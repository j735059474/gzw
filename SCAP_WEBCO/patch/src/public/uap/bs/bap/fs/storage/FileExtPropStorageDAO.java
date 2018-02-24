package uap.bs.bap.fs.storage;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nc.bs.logging.Logger;
import nc.jdbc.framework.ConnectionFactory;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.processor.BeanMappingListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.util.DBConsts;
import nc.jdbc.framework.util.DBUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.util.iufo.pub.OIDMaker;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import uap.pub.bap.fs.domain.FileStorageConfigFactory;
import uap.pub.bap.fs.domain.FileStorageException;
import uap.pub.bap.fs.domain.ext.FileStorageMappingMeta;
import uap.pub.bap.fs.domain.ext.FileStoreExtInfo;
import uap.pub.bap.fs.domain.ext.IFileStorageExt;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.perfwatch.PerfWatch;

/**
 * 扩展属性Dao
 * 
 * @author chenfeic
 * 
 */
public class FileExtPropStorageDAO {

	public static final String EXT_GUID = "guid";

	public static final String EXT_TABLENAME = "tableName";

	public static final String EXT_TYPE = "type";

	public static final String EXT_PK = "fld_id";

	/** 扩展属性表中headerId列 **/
	public static final String HEADER_COLUMN = "fld_headerId";

	public boolean isTableExisted(IMappingMeta meta) throws Exception {

		String tableName = meta.getTableName();
		String sql = "select count(1) from  sysobjects where name ='"
				+ tableName + "' and type='U'";

		JdbcSession jdbc = null;
		int iCount = -1;
		PersistenceManager persist = null;
		try {
			String dsName = FileStorageConfigFactory.getConfig().getDsName();
			persist = PersistenceManager.getInstance(dsName);
			jdbc = persist.getJdbcSession();

			// 获得数据库类型，效率优化
			int iDBType = jdbc.getDbType();
			if (iDBType == DBConsts.SQLSERVER) {
				sql = "select count(1) from  sysobjects where name ='"
						+ tableName + "' and type='U'";
				jdbc.setSQLTranslator(false);
			} else if (iDBType == DBConsts.ORACLE) {
				sql = "select count(1) from user_tables where table_name ='"
						+ tableName.toUpperCase() + "'";
				jdbc.setSQLTranslator(false);
			} else if (iDBType == DBConsts.DB2) {
				sql = "select count(1) from syscat.tables where tabschema = current schema and tabname = '"
						+ tableName.toUpperCase() + "'";
				jdbc.setSQLTranslator(false);
			} else if (iDBType == DBConsts.POSTGRESQL) {
				sql = "select count(1) from pg_class where relname = '"
						+ tableName.toLowerCase() + "'";// shit~~必须得小写
				jdbc.setSQLTranslator(false);
			}
			// 查询行数
			Integer count = (Integer) jdbc.executeQuery(sql,
					new ColumnProcessor(1));
			iCount = count.intValue();
		} catch (Exception e) {
			Logger.error("判断扩展属性对应的表是否存在时出错");
			Logger.error("[View existed]: 通过系统表访问表出错", e);
			return false;
		} finally {
			if (persist != null)
				persist.release();
		}
		return (iCount != 0);
	}

	/**
	 * 创建扩展属性对应的表
	 * 
	 * @param meta
	 */
	public void createTable(IMappingMeta meta) {
		String[] columns = meta.getColumns();
		String[] columnTypes = ((FileStorageMappingMeta) meta).getColumnTypes();
		if (ArrayUtils.isEmpty(columns) || ArrayUtils.isEmpty(columnTypes)
				|| columns.length != columnTypes.length) {
			String errorMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("8001006_0", "08001006-0354") /*
															 * @res *
															 * "列的个数和列对应的数据类型个数不一致"
															 */;
			throw new FileStorageException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001006_0", "08001006-0342") /*
																			 * @res
																			 * *
																			 * "创建扩展属性对应的表失败"
																			 */
					+ errorMsg);
		}
		String tableName = meta.getTableName().toUpperCase();
		String dsName = FileStorageConfigFactory.getConfig().getDsName();
		String pk = meta.getPrimaryKey();

		String attrSql = "";
		for (int i = 0; i < columns.length; i++) {// pk单独处理
			attrSql += columns[i] + " " + columnTypes[i] + ", ";
		}
		// 加上headerId字段
		attrSql += HEADER_COLUMN + " " + "VARCHAR(20)" + ", ";
		String sql = "CREATE TABLE " + tableName + "(" + pk
				+ " CHAR(20) NOT NULL, " + attrSql + " " + " PRIMARY KEY "
				+ "(" + pk + "))";

		JdbcSession jdbc = null;
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance(dsName);
			jdbc = persist.getJdbcSession();
			jdbc.executeUpdate(sql);
		} catch (Exception e) {
			AppDebug.error("创建扩展属性对应的表失败" + e);
			throw new FileStorageException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001006_0", "08001006-0342") /*
																			 * @res
																			 * *
																			 * "创建扩展属性对应的表失败"
																			 */
					+ e.getMessage());
		} finally {
			if (jdbc != null)
				jdbc.closeAll();
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * 查询扩展属性
	 * 
	 * @param extPk
	 *            PK值
	 * @param tableName
	 *            表名
	 * @param clzName
	 *            扩展属性的类型
	 * @return
	 */
	public IFileStorageExt getExtVo(String extPk, String tableName,
			String clzName) {
		String dsName = FileStorageConfigFactory.getConfig().getDsName();
		PersistenceManager persist = null;
		JdbcSession jdbcSession = null;
		try {
			persist = PersistenceManager.getInstance(dsName);
			jdbcSession = persist.getJdbcSession();
			jdbcSession.setSQLTranslator(false);// 不执行翻译。
			String sql = "Select * from " + tableName + " Where " + EXT_PK
					+ " = ?";
			SQLParameter params = new SQLParameter();
			params.addParam(extPk);
			Class<?> clz = Class.forName(clzName);
			IFileStorageExt extVO = (IFileStorageExt) clz.newInstance();
			Collection retrieve = (Collection) jdbcSession.executeQuery(sql,
					params, new BeanMappingListProcessor(clz,
							new FileStorageMappingMeta(extVO)));
			Iterator iterator = retrieve.iterator();
			IFileStorageExt next = null;
			while (iterator.hasNext()) {// 只会有一个
				next = (IFileStorageExt) iterator.next();
			}
			return next;
		} catch (Exception e) {
			AppDebug.error("查询扩展属性出错：" + e);
			throw new FileStorageException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001006_0", "08001006-0350") /*
																			 * @res
																			 * "查询扩展属性出错"
																			 */
					+ e.getMessage());
		} finally {
			if (jdbcSession != null)
				jdbcSession.closeAll();
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * 查询扩展属性
	 * 
	 * @param extInfos
	 *            一组文件扩展信息
	 * @return
	 */
	public IFileStorageExt[] getExtVo(FileStoreExtInfo... extInfos) {
		if (ArrayUtils.isEmpty(extInfos)) {
			return new IFileStorageExt[0];
		}
		List<IFileStorageExt> extList = new ArrayList<IFileStorageExt>();
		String dsName = FileStorageConfigFactory.getConfig().getDsName();
		PersistenceManager persist = null;
		JdbcSession jdbcSession = null;
		try {
			persist = PersistenceManager.getInstance(dsName);
			jdbcSession = persist.getJdbcSession();
			jdbcSession.setSQLTranslator(false);// 不执行翻译。
			for (FileStoreExtInfo extInfo : extInfos) {
				String sql = SQLHelper.getSelectSQL(extInfo.getExtVoTableName(),
						null, new String[] { EXT_PK });
				SQLParameter params = new SQLParameter();
				params.addParam(extInfo.getExtVoPK());
				Class<?> clz = Class.forName(extInfo.getExtVoType());
				IFileStorageExt extVO = (IFileStorageExt) clz.newInstance();
				Collection retrieve = (Collection) jdbcSession.executeQuery(
						sql, params, new BeanMappingListProcessor(clz,
								new FileStorageMappingMeta(extVO)));
				Iterator iterator = retrieve.iterator();
				IFileStorageExt next = null;
				while (iterator.hasNext()) {// 只会有一个
					next = (IFileStorageExt) iterator.next();
					extList.add(next);
				}
			}
			return extList.toArray(new IFileStorageExt[0]);
		} catch (Exception e) {
			AppDebug.error("查询扩展属性出错：" + e);
			throw new FileStorageException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001006_0", "08001006-0350") /*
																			 * @res
																			 * "查询扩展属性出错"
																			 */
					+ e.getMessage());
		} finally {
			if (jdbcSession != null)
				jdbcSession.closeAll();
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * 批量插入扩展属性信息
	 * 
	 * @param headerId
	 *            扩展属性对应的headr的pk值
	 * @param extVos
	 * @param meta
	 * @return 返回的是插入数据的pk,表名，类型信息
	 */
	public FileStoreExtInfo[] insertObject(String headerId,
			IFileStorageExt... extVos) {
		if (ArrayUtils.isEmpty(extVos)) {
			return new FileStoreExtInfo[0];
		}
		List<FileStoreExtInfo> extInfos = new ArrayList<FileStoreExtInfo>();

		String dsName = FileStorageConfigFactory.getConfig().getDsName();
		PersistenceManager persist = null;
		JdbcSession jdbcSession = null;

		try {
			persist = PersistenceManager.getInstance(dsName);
			jdbcSession = persist.getJdbcSession();
			jdbcSession.setSQLTranslator(false);// 不执行翻译。
//			jdbcSession.setAddTimeStamp(false);// 不要ts这个列,国资委补丁
			for (IFileStorageExt extVo : extVos) {
				IMappingMeta meta = new FileStorageMappingMeta(extVo);
				List<String> validateColumns = new ArrayList<String>();

				String pk = OIDMaker.getGE(
						FileStorageConfigFactory.getConfig().getDsName())
						.generate();// 字段生成PK
				String tableName = meta.getTableName().toUpperCase();
				String[] columns = meta.getColumns();// 列名
				// 查询sql加上PK
				validateColumns.add(meta.getPrimaryKey());
				SQLParameter parameter = new SQLParameter();
				// 参数中加上pk
				parameter.addParam(pk);
				String[] attributes = meta.getAttributes();
				for (int i = 0; i < attributes.length; i++) {
					// 反射得到属性的值
					Field field = extVo.getClass().getDeclaredField(
							attributes[i]);
					field.setAccessible(true);
					Object value = field.get(extVo);
					if (value != null) {// 如果值为空，则不插入
						validateColumns.add(columns[i]);
						parameter.addParam(value);
					}
				}
				validateColumns.add(HEADER_COLUMN);// 插入headerId列
				parameter.addParam(headerId);
				String insertSQL = SQLHelper.getInsertSQL(tableName,
						validateColumns.toArray(new String[0]));
				jdbcSession.addBatch(insertSQL, parameter);
				FileStoreExtInfo extInfo = new FileStoreExtInfo();
				extInfo.setExtVoPK(pk);
				extInfo.setExtVoTableName(tableName);
				extInfo.setExtVoType(extVo.getClass().getName());
				extInfos.add(extInfo);
			}
			jdbcSession.executeBatch();
			return extInfos.toArray(new FileStoreExtInfo[0]);
		} catch (Exception e) {
			AppDebug.error("插入扩展属性信息出错：" + e);
			throw new FileStorageException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001006_0", "08001006-0351") /*
																			 * @res
																			 * *
																			 * "插入扩展属性信息出错"
																			 */
					+ e.getMessage());
		} finally {
			if (jdbcSession != null)
				jdbcSession.closeAll();
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * 根据一组扩展属性，获取相应的头信息id。
	 * 
	 * @param fileExts
	 * @return
	 */
	public String[] getHeaderIDs(IFileStorageExt... fileExts) {
		if (ArrayUtils.isEmpty(fileExts)) {
			return new String[0];
		}
		List<String> headList = new ArrayList<String>();
		for (IFileStorageExt fileExt : fileExts) {
			String[] headerIds = getHeaderID(fileExt);
			if (!ArrayUtils.isEmpty(headerIds)) {
				for (String headerId : headerIds) {
					headList.add(headerId);
				}
			}
		}
		return headList.toArray(new String[0]);
	}

	/**
	 * 根据扩展属性获取相应头信息的ID。 虽然只是一个扩展属性，但是根据其值，我们其实可以查询多个header记录
	 * 
	 * @param fileExt
	 * @return
	 */
	private String[] getHeaderID(IFileStorageExt fileExt) {
		PerfWatch pw = new PerfWatch("后台查询文件头信息（by扩展属性）FileExtPropStorageDAO.getHeaderID(IFileStorageExt)");
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		pw.appendMessage("开始查询文件信息，查询时间为：" + dateFormat.format(new Date()));
		if (fileExt == null) {
			return new String[0];
		}
		IMappingMeta meta = new FileStorageMappingMeta(fileExt);

		String dsName = FileStorageConfigFactory.getConfig().getDsName();
		ResultSet rs = null;
		Connection conn = null;
		List<String> headerList = new ArrayList<String>();
		try {
			if (!isTableExisted(meta)) {// 如果扩展属性对应的表不存在，则直接返回null
				return new String[0];
			}
			conn = getConnection(dsName);
			pw.appendMessage("数据源为：" + dsName);
			// 得到插入的SQL语句
			String sql = "SELECT " + HEADER_COLUMN + " FROM "
					+ meta.getTableName() + " Where ";
			List<String> validateColumns = new ArrayList<String>();
			SQLParameter params = new SQLParameter();
			String[] attributes = meta.getAttributes();
			String[] columns = meta.getColumns();
			for (int i = 0; i < attributes.length; i++) {
				// 反射得到属性的值
				Field field = fileExt.getClass()
						.getDeclaredField(attributes[i]);
				field.setAccessible(true);
				Object value = field.get(fileExt);
				if (value != null) {// 如果值为空，则不插入
					if (!isZeroValue(value)) {
						validateColumns.add(columns[i]);
						params.addParam(value);
					}
				}
			}
			int size = validateColumns.size();
			if (size == 0) {
				return new String[0];
			}
			for (int i = 0; i < size; i++) {
				if (i != size - 1) {
					sql += validateColumns.get(i) + "=? And ";
				} else {
					sql += validateColumns.get(i) + "=?";
				}
			}
			pw.appendMessage("查询的sql语句为：" + sql);
			List parameters = params.getParameters();
			StringBuilder sb = new StringBuilder();
			if (parameters != null) {
				for (int i = 0; i < parameters.size(); i++) {
					sb.append(parameters.get(i)).append(",");
				}
			}
			pw.appendMessage("查询的sql参数为：" + sb.toString());
			PreparedStatement prepStatement = conn.prepareStatement(sql);
			prepStatement.clearParameters();
			if (params != null) {
				DBUtil.setStatementParameter(prepStatement, params);
			}
			prepStatement.setMaxRows(100000);
			prepStatement.setFetchSize(40);
			rs = prepStatement.executeQuery();
			StringBuilder sb1 = new StringBuilder();
			while (rs.next()) {
				String headerId = rs.getString(HEADER_COLUMN);
				if (!StringUtils.isEmpty(headerId)) {
					headerList.add(headerId);
					sb1.append(headerId).append(",");
				}
			}
			pw.appendMessage("查询成功,查询的文件头主键为：" + sb1.toString());
			return headerList.toArray(new String[0]);
		} catch (Exception e) {
			AppDebug.error("获取扩展属性相应头信息出错：" + e);
			throw new FileStorageException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001006_0", "08001006-0352") /*
																			 * @res
																			 * "获取扩展属性相应头信息出错"
																			 */
					+ e.getMessage());
		} finally {
			DBUtil.closeRs(rs);
			DBUtil.closeConnection(conn);
			pw.stop();
		}
	}

	/**
	 * 判断当前值是否为基本类型，且为0
	 * 
	 * @param value
	 * @return
	 */
	private boolean isZeroValue(Object value) {
		if (value instanceof Integer && ((Integer) value).intValue() == 0)
			return true;
		if (value instanceof Long && ((Long) value).intValue() == 0)
			return true;
		if (value instanceof Short && ((Short) value).intValue() == 0)
			return true;
		return false;

	}

	/**
	 * 批量删除扩展属性 注意使用使用此方法时，扩展属性的PK必须与表名一一对应，否则会导致删除失败
	 * 
	 * @param extPks
	 *            扩展属性的PK值
	 * @param tableName
	 *            扩展属性对应的表名
	 */
	public void deleteExtVo(FileStoreExtInfo[] extVoInfos) {
		if (ArrayUtils.isEmpty(extVoInfos)) {
			return;
		}
		String dsName = FileStorageConfigFactory.getConfig().getDsName();
		PersistenceManager persist = null;
		JdbcSession jdbcSession = null;
		try {
			persist = PersistenceManager.getInstance(dsName);
			jdbcSession = persist.getJdbcSession();
			jdbcSession.setSQLTranslator(false);// 不执行翻译。
			for (FileStoreExtInfo extVoInfo : extVoInfos) {
				String sql = "Delete  from " + extVoInfo.getExtVoTableName()
						+ " Where " + EXT_PK + " = ?";
				SQLParameter params = new SQLParameter();
				params.addParam(extVoInfo.getExtVoPK());
				jdbcSession.addBatch(sql, params);
			}

			jdbcSession.executeBatch();
		} catch (Exception e) {
			AppDebug.error("删除扩展属性时出错：" + e);
			throw new FileStorageException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001006_0", "08001006-0353") /*
																			 * @res
																			 * "删除扩展属性时出错"
																			 */
					+ e.getMessage());
		} finally {
			if (jdbcSession != null)
				jdbcSession.closeAll();
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @param dsName
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection(String dsName) throws SQLException {
		CrossDBConnection con = (CrossDBConnection) ConnectionFactory
				.getConnection(dsName);
		con.enableSQLTranslator(false);
		con.setAddTimeStamp(false);
		return con;
	}
}
