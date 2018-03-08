package nc.tc.nc.scap.pt.vos;
import org.testng.*;
import nc.vo.pub.*;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.Serializable;
import jxl.read.biff.BiffException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.yonyou.uat.util.ExcelDataProvider;
import com.yonyou.uat.util.DBDataProvider;
import com.yonyou.uat.dbmanagement.DBManage;
import com.yonyou.uat.dbmanagement.QueryInfoVO;
import nc.scap.pt.vos.ScapptInfoCountHVO;
import nc.bs.framework.common.NCLocator;
import com.yonyou.uat.framework.BaseTestCase;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDate;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDateTime;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import nc.scap.pt.vos.ScapptInfoCountBVO;
import java.lang.Integer;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDateTime;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import nc.vo.pub.IVOMeta;
public class ScapptInfoCountHVOTest extends BaseTestCase {
  ScapptInfoCountHVO scapptInfoCountHVO=null;
  DBManage dbManage=null;
  
  @BeforeClass 
  public void BeforeClass(){
    scapptInfoCountHVO=NCLocator.getInstance().lookup(ScapptInfoCountHVO.class);
  }
  
  @AfterClass 
  public void AfterClass(){
  }
  
  @BeforeMethod 
  public void BeforeMethod(){
    List<String> tableList=new ArrayList<String>();
    tableList.add("pub_wfexptlog");
    dbManage=new DBManage();
    dbManage.setTableList(tableList);
    dbManage.tableExport();
  }
  
  @AfterMethod 
  public void AfterMethod(){
    dbManage.tableRollBack();
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getPk_org(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getPk_org();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setPk_org(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newPk_org=(String)dp.get("newpk_org").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setPk_org(newPk_org);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getPk_group(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getPk_group();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setPk_group(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newPk_group=(String)dp.get("newpk_group").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setPk_group(newPk_group);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getPk_infocount(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getPk_infocount();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setPk_infocount(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newPk_infocount=(String)dp.get("newpk_infocount").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setPk_infocount(newPk_infocount);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getBegindate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    UFDate retObj=null;
    retObj=scapptInfoCountHVO.getBegindate();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertNotNull(retObj);
    Assert.assertNotNull(retObj.getDay());
    Assert.assertEquals(retObj.getDay(),0);
    Assert.assertNotNull(retObj.getDaysMonth());
    Assert.assertEquals(retObj.getDaysMonth(),0);
    Assert.assertNotNull(retObj.getEnMonth());
    Assert.assertEquals(retObj.getEnMonth(),"expectValue");
    Assert.assertNotNull(retObj.getEnWeek());
    Assert.assertEquals(retObj.getEnWeek(),"expectValue");
    Assert.assertNotNull(retObj.getLocalDay());
    Assert.assertEquals(retObj.getLocalDay(),0);
    Assert.assertNotNull(retObj.getLocalMonth());
    Assert.assertEquals(retObj.getLocalMonth(),0);
    Assert.assertNotNull(retObj.getLocalYear());
    Assert.assertEquals(retObj.getLocalYear(),0);
    Assert.assertNotNull(retObj.getMillis());
    Assert.assertEquals(retObj.getMillis(),0);
    Assert.assertNotNull(retObj.getMonth());
    Assert.assertEquals(retObj.getMonth(),0);
    Assert.assertNotNull(retObj.getStrDay());
    Assert.assertEquals(retObj.getStrDay(),"expectValue");
    Assert.assertNotNull(retObj.getStrMonth());
    Assert.assertEquals(retObj.getStrMonth(),"expectValue");
    Assert.assertNotNull(retObj.getWeek());
    Assert.assertEquals(retObj.getWeek(),0);
    Assert.assertNotNull(retObj.getWeekOfYear());
    Assert.assertEquals(retObj.getWeekOfYear(),0);
    Assert.assertNotNull(retObj.getYear());
    Assert.assertEquals(retObj.getYear(),0);
    
    //Verify Return or middle Object == expect Object(from object file)
    Object expectedObj=super.getExpectResultObject("caseName");
    if (expectedObj != null) {
      Assert.assertEquals(retObj,expectedObj);
    }
 else {
      super.saveResultObject((Serializable)retObj,"caseName");
    }
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setBegindate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    nc.vo.pub.lang.UFDate newBegindate=(nc.vo.pub.lang.UFDate)super.getSpringObj("nc.vo.pub.lang.ufdate");
    
    //Invoke tested method
    scapptInfoCountHVO.setBegindate(newBegindate);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getEnddate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    UFDate retObj=null;
    retObj=scapptInfoCountHVO.getEnddate();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertNotNull(retObj);
    Assert.assertNotNull(retObj.getDay());
    Assert.assertEquals(retObj.getDay(),0);
    Assert.assertNotNull(retObj.getDaysMonth());
    Assert.assertEquals(retObj.getDaysMonth(),0);
    Assert.assertNotNull(retObj.getEnMonth());
    Assert.assertEquals(retObj.getEnMonth(),"expectValue");
    Assert.assertNotNull(retObj.getEnWeek());
    Assert.assertEquals(retObj.getEnWeek(),"expectValue");
    Assert.assertNotNull(retObj.getLocalDay());
    Assert.assertEquals(retObj.getLocalDay(),0);
    Assert.assertNotNull(retObj.getLocalMonth());
    Assert.assertEquals(retObj.getLocalMonth(),0);
    Assert.assertNotNull(retObj.getLocalYear());
    Assert.assertEquals(retObj.getLocalYear(),0);
    Assert.assertNotNull(retObj.getMillis());
    Assert.assertEquals(retObj.getMillis(),0);
    Assert.assertNotNull(retObj.getMonth());
    Assert.assertEquals(retObj.getMonth(),0);
    Assert.assertNotNull(retObj.getStrDay());
    Assert.assertEquals(retObj.getStrDay(),"expectValue");
    Assert.assertNotNull(retObj.getStrMonth());
    Assert.assertEquals(retObj.getStrMonth(),"expectValue");
    Assert.assertNotNull(retObj.getWeek());
    Assert.assertEquals(retObj.getWeek(),0);
    Assert.assertNotNull(retObj.getWeekOfYear());
    Assert.assertEquals(retObj.getWeekOfYear(),0);
    Assert.assertNotNull(retObj.getYear());
    Assert.assertEquals(retObj.getYear(),0);
    
    //Verify Return or middle Object == expect Object(from object file)
    Object expectedObj=super.getExpectResultObject("caseName");
    if (expectedObj != null) {
      Assert.assertEquals(retObj,expectedObj);
    }
 else {
      super.saveResultObject((Serializable)retObj,"caseName");
    }
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setEnddate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    nc.vo.pub.lang.UFDate newEnddate=(nc.vo.pub.lang.UFDate)super.getSpringObj("nc.vo.pub.lang.ufdate");
    
    //Invoke tested method
    scapptInfoCountHVO.setEnddate(newEnddate);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getTransjg(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getTransjg();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setTransjg(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newTransjg=(String)dp.get("newtransjg").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setTransjg(newTransjg);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getJgtype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getJgtype();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setJgtype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newJgtype=(String)dp.get("newjgtype").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setJgtype(newJgtype);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getVunit(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getVunit();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setVunit(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVunit=(String)dp.get("newvunit").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setVunit(newVunit);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getBiller(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getBiller();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setBiller(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newBiller=(String)dp.get("newbiller").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setBiller(newBiller);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getBilldate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    UFDateTime retObj=null;
    retObj=scapptInfoCountHVO.getBilldate();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertNotNull(retObj);
    Assert.assertNotNull(retObj.getBeginDate());
    Assert.assertNotNull(retObj.getDate());
    Assert.assertNotNull(retObj.getDay());
    Assert.assertEquals(retObj.getDay(),0);
    Assert.assertNotNull(retObj.getDaysMonth());
    Assert.assertEquals(retObj.getDaysMonth(),0);
    Assert.assertNotNull(retObj.getEnMonth());
    Assert.assertEquals(retObj.getEnMonth(),"expectValue");
    Assert.assertNotNull(retObj.getEnWeek());
    Assert.assertEquals(retObj.getEnWeek(),"expectValue");
    Assert.assertNotNull(retObj.getEndDate());
    Assert.assertNotNull(retObj.getHour());
    Assert.assertEquals(retObj.getHour(),0);
    Assert.assertNotNull(retObj.getLocalDay());
    Assert.assertEquals(retObj.getLocalDay(),0);
    Assert.assertNotNull(retObj.getLocalHour());
    Assert.assertEquals(retObj.getLocalHour(),0);
    Assert.assertNotNull(retObj.getLocalMinute());
    Assert.assertEquals(retObj.getLocalMinute(),0);
    Assert.assertNotNull(retObj.getLocalMonth());
    Assert.assertEquals(retObj.getLocalMonth(),0);
    Assert.assertNotNull(retObj.getLocalYear());
    Assert.assertEquals(retObj.getLocalYear(),0);
    Assert.assertNotNull(retObj.getMillis());
    Assert.assertEquals(retObj.getMillis(),0);
    Assert.assertNotNull(retObj.getMinute());
    Assert.assertEquals(retObj.getMinute(),0);
    Assert.assertNotNull(retObj.getMonth());
    Assert.assertEquals(retObj.getMonth(),0);
    Assert.assertNotNull(retObj.getSecond());
    Assert.assertEquals(retObj.getSecond(),0);
    Assert.assertNotNull(retObj.getStrDay());
    Assert.assertEquals(retObj.getStrDay(),"expectValue");
    Assert.assertNotNull(retObj.getStrMonth());
    Assert.assertEquals(retObj.getStrMonth(),"expectValue");
    Assert.assertNotNull(retObj.getTime());
    Assert.assertEquals(retObj.getTime(),"expectValue");
    Assert.assertNotNull(retObj.getUFTime());
    Assert.assertNotNull(retObj.getWeek());
    Assert.assertEquals(retObj.getWeek(),0);
    Assert.assertNotNull(retObj.getWeekOfYear());
    Assert.assertEquals(retObj.getWeekOfYear(),0);
    Assert.assertNotNull(retObj.getYear());
    Assert.assertEquals(retObj.getYear(),0);
    
    //Verify Return or middle Object == expect Object(from object file)
    Object expectedObj=super.getExpectResultObject("caseName");
    if (expectedObj != null) {
      Assert.assertEquals(retObj,expectedObj);
    }
 else {
      super.saveResultObject((Serializable)retObj,"caseName");
    }
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setBilldate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    nc.vo.pub.lang.UFDateTime newBilldate=(nc.vo.pub.lang.UFDateTime)super.getSpringObj("nc.vo.pub.lang.ufdatetime");
    
    //Invoke tested method
    scapptInfoCountHVO.setBilldate(newBilldate);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getDef1(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getDef1();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setDef1(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newDef1=(String)dp.get("newdef1").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setDef1(newDef1);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getDef2(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getDef2();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setDef2(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newDef2=(String)dp.get("newdef2").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setDef2(newDef2);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getDef3(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getDef3();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setDef3(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newDef3=(String)dp.get("newdef3").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setDef3(newDef3);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getDef4(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getDef4();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setDef4(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newDef4=(String)dp.get("newdef4").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setDef4(newDef4);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getDef5(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getDef5();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setDef5(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newDef5=(String)dp.get("newdef5").get(0);
    
    //Invoke tested method
    scapptInfoCountHVO.setDef5(newDef5);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getId_scappt_infocount_b(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    ScapptInfoCountBVO[] retObj={};
    retObj=scapptInfoCountHVO.getId_scappt_infocount_b();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertNotNull(retObj);
    Assert.assertEquals(retObj.length,0);
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setId_scappt_infocount_b(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    ScapptInfoCountBVO[] newId_scappt_infocount_b={};
    
    //Invoke tested method
    scapptInfoCountHVO.setId_scappt_infocount_b(newId_scappt_infocount_b);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getDr(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    Integer retObj=0;
    retObj=scapptInfoCountHVO.getDr();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertTrue(retObj.equals(0));
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setDr(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    Integer newDr=Integer.parseInt(dp.get("newdr").get(0));
    
    //Invoke tested method
    scapptInfoCountHVO.setDr(newDr);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getTs(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    UFDateTime retObj=null;
    retObj=scapptInfoCountHVO.getTs();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertNotNull(retObj);
    Assert.assertNotNull(retObj.getBeginDate());
    Assert.assertNotNull(retObj.getDate());
    Assert.assertNotNull(retObj.getDay());
    Assert.assertEquals(retObj.getDay(),0);
    Assert.assertNotNull(retObj.getDaysMonth());
    Assert.assertEquals(retObj.getDaysMonth(),0);
    Assert.assertNotNull(retObj.getEnMonth());
    Assert.assertEquals(retObj.getEnMonth(),"expectValue");
    Assert.assertNotNull(retObj.getEnWeek());
    Assert.assertEquals(retObj.getEnWeek(),"expectValue");
    Assert.assertNotNull(retObj.getEndDate());
    Assert.assertNotNull(retObj.getHour());
    Assert.assertEquals(retObj.getHour(),0);
    Assert.assertNotNull(retObj.getLocalDay());
    Assert.assertEquals(retObj.getLocalDay(),0);
    Assert.assertNotNull(retObj.getLocalHour());
    Assert.assertEquals(retObj.getLocalHour(),0);
    Assert.assertNotNull(retObj.getLocalMinute());
    Assert.assertEquals(retObj.getLocalMinute(),0);
    Assert.assertNotNull(retObj.getLocalMonth());
    Assert.assertEquals(retObj.getLocalMonth(),0);
    Assert.assertNotNull(retObj.getLocalYear());
    Assert.assertEquals(retObj.getLocalYear(),0);
    Assert.assertNotNull(retObj.getMillis());
    Assert.assertEquals(retObj.getMillis(),0);
    Assert.assertNotNull(retObj.getMinute());
    Assert.assertEquals(retObj.getMinute(),0);
    Assert.assertNotNull(retObj.getMonth());
    Assert.assertEquals(retObj.getMonth(),0);
    Assert.assertNotNull(retObj.getSecond());
    Assert.assertEquals(retObj.getSecond(),0);
    Assert.assertNotNull(retObj.getStrDay());
    Assert.assertEquals(retObj.getStrDay(),"expectValue");
    Assert.assertNotNull(retObj.getStrMonth());
    Assert.assertEquals(retObj.getStrMonth(),"expectValue");
    Assert.assertNotNull(retObj.getTime());
    Assert.assertEquals(retObj.getTime(),"expectValue");
    Assert.assertNotNull(retObj.getUFTime());
    Assert.assertNotNull(retObj.getWeek());
    Assert.assertEquals(retObj.getWeek(),0);
    Assert.assertNotNull(retObj.getWeekOfYear());
    Assert.assertEquals(retObj.getWeekOfYear(),0);
    Assert.assertNotNull(retObj.getYear());
    Assert.assertEquals(retObj.getYear(),0);
    
    //Verify Return or middle Object == expect Object(from object file)
    Object expectedObj=super.getExpectResultObject("caseName");
    if (expectedObj != null) {
      Assert.assertEquals(retObj,expectedObj);
    }
 else {
      super.saveResultObject((Serializable)retObj,"caseName");
    }
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void setTs(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    nc.vo.pub.lang.UFDateTime newTs=(nc.vo.pub.lang.UFDateTime)super.getSpringObj("nc.vo.pub.lang.ufdatetime");
    
    //Invoke tested method
    scapptInfoCountHVO.setTs(newTs);
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals("actual","expected");
    Assert.assertNotNull("actual");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getParentPKFieldName(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getParentPKFieldName();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getPKFieldName(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getPKFieldName();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getTableName(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptInfoCountHVO.getTableName();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getDefaultTableName(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=ScapptInfoCountHVO.getDefaultTableName();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertEquals(retObj,"expectValue");
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getMetaData(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    IVOMeta retObj=null;
    retObj=scapptInfoCountHVO.getMetaData();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertNotNull(retObj);
    Assert.assertNotNull(retObj.getAttributes());
    Assert.assertNotNull(retObj.getBusinessAttribute());
    Assert.assertEquals(retObj.getBusinessAttribute().size(),0);
    Assert.assertNotNull(retObj.getEntityName());
    Assert.assertEquals(retObj.getEntityName(),"expectValue");
    Assert.assertNotNull(retObj.getLabel());
    Assert.assertEquals(retObj.getLabel(),"expectValue");
    Assert.assertNotNull(retObj.getPrimaryAttribute());
    Assert.assertNotNull(retObj.getStatisticInfo());
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
}
