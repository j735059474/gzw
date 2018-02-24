--视图  v_userpsn 公共视图 通过组织选择用户，右侧用户列表显示cp_user和bd_psndoc等信息 

create or replace view v_userpsn as
select u.cuserid,u.user_code,u.user_name,o.name szdw,psn.mobile ,psn.officephone tel,psn.email,
 case psn.sex when 1 then '男' else  '女' end sex,j.jobname zw,'职称111' zc,o.pk_org,j.pk_psnjob pk_zw,'' pk_zc,u.user_type,u.enablestate,u.dr,u.ts
  ,u.pk_dept,p.name deptname
from cp_user u
join bd_psndoc psn on psn.pk_psndoc=u.pk_base_doc
join cp_orgs o on o.pk_org=u.pk_org
join org_dept p on p.pk_dept=u.pk_dept
left join bd_psnjob j on j.pk_psndoc=psn.pk_psndoc
where u.dr=0 and o.dr=0;
