--一下内容以移至 scapco模块

--导出补丁的时候注意 数据库的

--表 scap_refinfo 校验删除的数据是否被引用需要的表


-- Create table
create table SCAP_REFINFO
(
  table1  VARCHAR2(50) not null,
  pkfield VARCHAR2(50) not null,
  name1   VARCHAR2(50),
  table2  VARCHAR2(50) not null,
  fkfield VARCHAR2(50) not null,
  name2   VARCHAR2(50),
  msg     VARCHAR2(100),
  dr      INTEGER,
  ts      CHAR(19)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Add comments to the columns 
comment on column SCAP_REFINFO.table1
  is '被引用表名';
comment on column SCAP_REFINFO.pkfield
  is '与引用表关联的字段';
comment on column SCAP_REFINFO.name1
  is '被引用表单据名称';
comment on column SCAP_REFINFO.table2
  is '引用表名';
comment on column SCAP_REFINFO.fkfield
  is '与被引用表关联的字段';
comment on column SCAP_REFINFO.name2
  is '引用表单据名称';
comment on column SCAP_REFINFO.msg
  is '被引用则提示';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SCAP_REFINFO
  add constraint INDEX_SCAP_REFINFO primary key (TABLE1, PKFIELD, TABLE2, FKFIELD)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
 
