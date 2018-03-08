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
import nc.scap.pt.vos.ScapptApplyHVO;
import nc.bs.framework.common.NCLocator;
import com.yonyou.uat.framework.BaseTestCase;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDateTime;
import java.lang.String;
import java.lang.String;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDateTime;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
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
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import nc.scap.pt.vos.ScapptApplyBVO;
import nc.scap.pt.vos.ScapptApplyAttachVO;
import java.lang.Integer;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDateTime;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import java.lang.String;
import nc.vo.pub.IVOMeta;
public class ScapptApplyHVOTest extends BaseTestCase {
  ScapptApplyHVO scapptApplyHVO=null;
  DBManage dbManage=null;
  
  @BeforeClass 
  public void BeforeClass(){
    scapptApplyHVO=NCLocator.getInstance().lookup(ScapptApplyHVO.class);
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
  public void getPk_apply_h(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getPk_apply_h();
    
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
  public void setPk_apply_h(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newPk_apply_h=(String)dp.get("newpk_apply_h").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setPk_apply_h(newPk_apply_h);
    
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
  public void getPk_org(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getPk_org();
    
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
    scapptApplyHVO.setPk_org(newPk_org);
    
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
    retObj=scapptApplyHVO.getPk_group();
    
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
    scapptApplyHVO.setPk_group(newPk_group);
    
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
  public void getBillmaker(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getBillmaker();
    
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
  public void setBillmaker(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newBillmaker=(String)dp.get("newbillmaker").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setBillmaker(newBillmaker);
    
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
  public void getBillmakedate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    UFDateTime retObj=null;
    retObj=scapptApplyHVO.getBillmakedate();
    
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
  public void setBillmakedate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    nc.vo.pub.lang.UFDateTime newBillmakedate=(nc.vo.pub.lang.UFDateTime)super.getSpringObj("nc.vo.pub.lang.ufdatetime");
    
    //Invoke tested method
    scapptApplyHVO.setBillmakedate(newBillmakedate);
    
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
  public void getBillno(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getBillno();
    
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
  public void setBillno(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newBillno=(String)dp.get("newbillno").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setBillno(newBillno);
    
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
  public void getApprover(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getApprover();
    
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
  public void setApprover(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newApprover=(String)dp.get("newapprover").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setApprover(newApprover);
    
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
  public void getApprovedate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    UFDateTime retObj=null;
    retObj=scapptApplyHVO.getApprovedate();
    
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
  public void setApprovedate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    nc.vo.pub.lang.UFDateTime newApprovedate=(nc.vo.pub.lang.UFDateTime)super.getSpringObj("nc.vo.pub.lang.ufdatetime");
    
    //Invoke tested method
    scapptApplyHVO.setApprovedate(newApprovedate);
    
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
  public void getFormstate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getFormstate();
    
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
  public void setFormstate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newFormstate=(String)dp.get("newformstate").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setFormstate(newFormstate);
    
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
  public void getFormtitle(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getFormtitle();
    
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
  public void setFormtitle(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newFormtitle=(String)dp.get("newformtitle").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setFormtitle(newFormtitle);
    
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
  public void getVrequestno(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVrequestno();
    
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
  public void setVrequestno(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVrequestno=(String)dp.get("newvrequestno").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVrequestno(newVrequestno);
    
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
  public void getCprocesstype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getCprocesstype();
    
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
  public void setCprocesstype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newCprocesstype=(String)dp.get("newcprocesstype").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setCprocesstype(newCprocesstype);
    
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
  public void getCchangetype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getCchangetype();
    
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
  public void setCchangetype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newCchangetype=(String)dp.get("newcchangetype").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setCchangetype(newCchangetype);
    
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
  public void getCtradetype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getCtradetype();
    
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
  public void setCtradetype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newCtradetype=(String)dp.get("newctradetype").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setCtradetype(newCtradetype);
    
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
  public void getCgreattype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getCgreattype();
    
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
  public void setCgreattype(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newCgreattype=(String)dp.get("newcgreattype").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setCgreattype(newCgreattype);
    
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
  public void getPk_rorg(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getPk_rorg();
    
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
  public void setPk_rorg(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newPk_rorg=(String)dp.get("newpk_rorg").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setPk_rorg(newPk_rorg);
    
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
  public void getVapproveno(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVapproveno();
    
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
  public void setVapproveno(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVapproveno=(String)dp.get("newvapproveno").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVapproveno(newVapproveno);
    
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
  public void getCapplicant(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getCapplicant();
    
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
  public void setCapplicant(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newCapplicant=(String)dp.get("newcapplicant").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setCapplicant(newCapplicant);
    
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
  public void getCapplicantdate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    UFDateTime retObj=null;
    retObj=scapptApplyHVO.getCapplicantdate();
    
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
  public void setCapplicantdate(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    nc.vo.pub.lang.UFDateTime newCapplicantdate=(nc.vo.pub.lang.UFDateTime)super.getSpringObj("nc.vo.pub.lang.ufdatetime");
    
    //Invoke tested method
    scapptApplyHVO.setCapplicantdate(newCapplicantdate);
    
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
  public void getVdef1(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef1();
    
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
  public void setVdef1(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef1=(String)dp.get("newvdef1").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef1(newVdef1);
    
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
  public void getVdef2(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef2();
    
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
  public void setVdef2(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef2=(String)dp.get("newvdef2").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef2(newVdef2);
    
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
  public void getVdef3(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef3();
    
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
  public void setVdef3(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef3=(String)dp.get("newvdef3").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef3(newVdef3);
    
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
  public void getVdef4(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef4();
    
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
  public void setVdef4(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef4=(String)dp.get("newvdef4").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef4(newVdef4);
    
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
  public void getVdef5(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef5();
    
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
  public void setVdef5(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef5=(String)dp.get("newvdef5").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef5(newVdef5);
    
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
  public void getVdef6(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef6();
    
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
  public void setVdef6(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef6=(String)dp.get("newvdef6").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef6(newVdef6);
    
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
  public void getVdef7(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef7();
    
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
  public void setVdef7(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef7=(String)dp.get("newvdef7").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef7(newVdef7);
    
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
  public void getVdef8(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef8();
    
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
  public void setVdef8(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef8=(String)dp.get("newvdef8").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef8(newVdef8);
    
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
  public void getVdef9(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef9();
    
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
  public void setVdef9(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef9=(String)dp.get("newvdef9").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef9(newVdef9);
    
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
  public void getVdef10(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    String retObj="";
    retObj=scapptApplyHVO.getVdef10();
    
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
  public void setVdef10(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    String newVdef10=(String)dp.get("newvdef10").get(0);
    
    //Invoke tested method
    scapptApplyHVO.setVdef10(newVdef10);
    
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
  public void getId_scappt_apply_b(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    ScapptApplyBVO[] retObj={};
    retObj=scapptApplyHVO.getId_scappt_apply_b();
    
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
  public void setId_scappt_apply_b(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    ScapptApplyBVO[] newId_scappt_apply_b={};
    
    //Invoke tested method
    scapptApplyHVO.setId_scappt_apply_b(newId_scappt_apply_b);
    
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
  public void getId_scappt_apply_attach(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    ScapptApplyAttachVO[] retObj={};
    retObj=scapptApplyHVO.getId_scappt_apply_attach();
    
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
  public void setId_scappt_apply_attach(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    ScapptApplyAttachVO[] newId_scappt_apply_attach={};
    
    //Invoke tested method
    scapptApplyHVO.setId_scappt_apply_attach(newId_scappt_apply_attach);
    
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
    retObj=scapptApplyHVO.getDr();
    
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
    scapptApplyHVO.setDr(newDr);
    
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
    retObj=scapptApplyHVO.getTs();
    
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
    scapptApplyHVO.setTs(newTs);
    
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
    retObj=scapptApplyHVO.getParentPKFieldName();
    
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
    retObj=scapptApplyHVO.getPKFieldName();
    
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
    retObj=scapptApplyHVO.getTableName();
    
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
    retObj=ScapptApplyHVO.getDefaultTableName();
    
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
    retObj=scapptApplyHVO.getMetaData();
    
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
