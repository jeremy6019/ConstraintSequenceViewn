package definition;

public class DataBase {
/*
  
 ** E- R Diagram (개체 관계도) 
 E : Entity 
 R: Relation  
 
 Foreign Key설정 방법 
 => 테이블의 Cardinality(대응수) 파악
 => 1:1 인경우 양쪽의 기본키르ㅜㄹ 다른 테이블의 외래키로 추가
 => 1:N 인경우 1쪽의 기본키를 N쪽에 외래키로 추가 
 => N:N 인경우에는 양쪽의 기본키를 외래키로 갖는 별도의 테이블 생성 
 
 테이블 분할 -Normalization(정규화)
 테이블 다시 합치는 것 : 역정규화 
 
 ** 제약조건(Constraint)
 =>테이블에 적합하지 않은 데이터가 저장되지 않도록 하기 위해서 설정하는 조건 
 1. 종류 
 1) 규칙 
 => NOT NULL :필수 입력
 => UNIQUE: 유일해야 합니다. 중복될 수 없다 
 => PRIMARI KEY: NOT NULL 이고 UNIQUE, 테이블에서 1개만 설정 가능 
 => FOREIGN KEY: 다른 테이블의 데이터를 참조하기 위해서 설정 
      다른 테이블에서 UNIQUE이거나 PRIMARY KEY인 경우만 설정 가능 
 => CHECK: 저장 가능한 값의 범위나 조건을 설정 
 => DEFAULT: 기본 값을 설정 
 
 2) 작성 위치
 => 컬럼 제약 조건: 컬럼을 만들때 설정하는 제약조건 
 => 테이블 제약조건: 컬럼을 전부 만들고 난후 설정하는 제약조건 
 => NOT NULL만은 컬럼 제약조건으로만 가능
 NOT NULL은 NULL을 표시하는 1bit코드를 추가할 것인지 아닌지를 설정하는 
 것이라서 컬럼을 만들때 설정해야 합니다.
 데이터베이스에서 NULL을 저장하는 방법은 실제로 NULL을 저장하는 것이 아니고
 NULL을 표시하는 하나의 bit를 추가해서 bit가 true이면 NULL이고, 
 False이면 NULL이아님    
 
 2. 제약조건 확인 
 =>USER_CONSTRAINTS테이블에 제약조건의 내용이 작성되어 있음 
 
 실습 
 => DEPT테이블에 DEPTNO가 10이고 DNAME이 비서이고 LOC가 SEOUL인 데이터를 삽입 
 
 INSERT INTO DEPT(DEPTNO, DNAME, LOC)
 VALUES(10,'비서','SEOUL'); 
  
 삽입하려고 하면 에러가 발생 
 무결성 재약조건 위반이라는 에러가 발생 
 -- DEPT 테이블에 DEPTNO가 PRIMARY KEY로
-- 설정되어 있어서 중복되거나 NULL일 수 없음 
 
 --제약조건 확인select * 
SELECT *
FROM USER_CONSTRAINTS; 

 
 1) 제약조건 종류 -CONSTRAINT_TYPE
 P: Primary Key 
 R: Foreign Key - 참조가 영어로 Reference라서 R로 표시 
 U: Unique 
 C: Check, Not Null 
  
3. 컬럼 레벨 제약조건 
=> 테이블을 생성할 때 컬럼의 자료형 뒤에 제약조건을 기재 
=> NOT NULL은 컬럼 레벨 제약조건으로만 설정이 가능 
컬럼이름 자료형 constraint 제약조건이름 제약조건 종류    
=>constraint 제약조건이름 은 생략이 가능한데 생략하면 오라클이 이름을 설정 
제약조건을 작성할 때는 NOT NULL, UNIQUE, PRIMARY KEY, REFERENCES
참조할테이블이름(컬럼이름) 의 형태로 작성 
CHECK(컬럼이름 조건) 의 형태로 설정 
=>gender컬럼은 남자와 여자의 값만을 가져야 한다. 
CHECK(gender in('남자', '여자'))
=>sal컬럼의 값은 0부터 3000사이어야 한다.
CHECK(sal between 0 and 3000) 

실습: 회원 테이블 생성 
기존의 테이블이 있으면 삭제 
- DROP TABLE MEMBER;

email - 문자 30자리 (변하지 않음) : 기본키 
pw - 문자 20자리(자주 변경) : 필수 
alias - 문자 20자리(자주 변경) : 필수 
gender - 문자 6자리(변경되지 않음) : 남자와 여자의 값만 가져야함 
birthday - 날짜: 기본값은 오늘 날짜 

CREATE TABLE MEMBER(
  컬럼이음 자료형, 제약조건,
  ... );
  
  CREATE TABLE MEMBER(
  email varchar2(30) PRIMARY KEY,
  password char(20) NOT NULL,
  alias char(20) NOT NULL,
  gender varchar2(6) check(gender IN ('남자','여자')),
  birthday DATE DEFAULT sysdate);

4. 테이블 제약 조건 
=> 컬럼을 전부 생성한 후에 제약조건을 설정 
=> NOT NULL은 테이블 제약조건으로 설정할 수 없습니다. 
=>반드시 테이블 제약조건으로 설정해야하는 경우는 PRIMARY KEY를 2개이상의
컬럼의 조합으로 만드는 경우에는 반드시 테이블 제약조건으로 만들어야 합니다.
하나의 테이블 생성구문에서 PRIMARY KEY라는 단어는 2번 이상 나오면 안됩니다. 

5. 외래키 설정 
=> references 참조할테이블이름(참조할 컬럼이름) 옵션설정
1) 옵션
=> ON DELETE CASCADE : 부모 테이블에서 삭제되면 같이 삭제 
=> ON DELETE SET NULL : 부모 테이블에서 삭제되면 NULL로 설정 
=> ON UPDATE CASCADE : 부모 테이블에서 수정되면 같이 수정 
=> ON UPDATE SET NULL : 부모 테이블에서 수정되면 NULL로 수정 
=>옵션을 생략하면 참조하는 테이블에서 참조된 데이터는 삭제할 수 없습니다.
게시판 글을 작성한 회원은 회원을 탈퇴할 수 없습니다.  

2) 테이블 제약조건으로 만들때는 FOREIGN KEY(컬럼이름) REFERENCES 참조할
테이블이름(참조할컬럼이름) 옵션의 형태로 설정 

실습 - 게시판 테이블(BOARD)  
=> 존재하면 삭제 
DROP TABLE BOARD; 

num - 글번호인데 정수 5자리까지: 기본키 
title - 제목인데 문자 100자리(변경안됨): 필수  
content - 내용인데 1000자 이상 
email - 문자 30자리: MEMBER 테이블의 EMAIL을 참조 
                                 삭제될 때 같이 삭제 
readcnt - 조회수인데 정수 5자리: 기본값이 0 

=> 가능한 것들은 테이블 제약조건으로 설정 

create table board(
  num number(5),
  title varchar2(100) not null,
  content clob,
  email varchar2(30), 
  readcnt number(5) default 0,
  primary key(num),
  foreign key(email) references member(email));
 
=>member테이블 삭제 
DROP TABLE MEMBER; 
 삭제가 안되고 에러 메세지가 출력 
 다른 테이블에서 참조하고 있는 테이블은 삭제할 수 없습니다. 
 
=>옵션 없이 FOREIGN KEY를 설정하게 되면 자식 테이블에서 사용중인 데이터는 
삭제할 수 없습니다. 
FOREIGN KEY를 설정할 때는 부모 테이블의 데이터가 삭제될 때 자식테이블에서 
어떻게 동작해야 하는지 고려를 해서 설정 
보통은 SET NULL을 많이 합니다. 

실습:  MEMBER 테이블에 2개의 테이터를 추가 

insert into member(email,pw,alias,gender)
values('jeremy94@naver.com','1234','붉은곰','남자');

insert into member(email,pw,alias,gender)
values('kys123@google.com','4321','예쁜공주','여자');

insert into board(num,title,content,email)
values(1,'제목','내용','jeremy94@daum.net');

위와 같은 데이터를 삽입하려고 하면 에러가 발생 
외래키는 null이거나 부모테이블에 존재하는 값만 삽입이 가능 
이러한 제약조건을 참조 무결성 제약조건이라고 합니다. 

=> 새로운 데이터 입력 : 존쟈하는 email이므로 데이터 삽입 성공 
insert into board(num,title,content,email)
values(1,'하오','내용1','jeremy94@	naver.com');

=>Member 테이블에서 email이 jeremy94@naver.com 인데이터 삭제 
DELETE FROM MEMBER 
WHERE email = 'jeremy94@naver.com';

=>Member 테이블에서 email이 kys123@google.com 인데이터 삭제 
DELETE FROM MEMBER 
WHERE email = 'kys123@google.com';

두번째 데이터는 삭제가 되지만 첫번째 데이터는 삭제가 안됨 
FOREIGN KEY를 설정할 때 옵션없이 설정하면 자식테이블에서 사용중인 데이터
는 삭제가 안됩니다, 
  
 create table board(
  num number(5),
  title varchar2(100) not null,
  content clob,
  email varchar2(30), 
  readcnt number(5) default 0,
  primary key(num),
  foreign key(email) 
      references member(email)
      ON DELETE SET null); 

=> 문법에 맞게 삽입하는 문장이나 삭제하는 문장을 작성했는데 에러가 나면 제약조건을 확인 
삽입하는 경우는 NOT NULL, UNIQUE, PRIMARY KEY, FOREIGN KEY, CHECK도 확인해 
봐야 합니다. 
삭제하는 경우는 FOREIGN KEY 만 확인해보면 됩니다.    

6. 제약조건 수정 
1)제약조건 추가 
=> 테이블을 만들고 데이터가 1개도 없다면 테이블을 새로 생성해도 됨 
=>테이블을 만들고 데이터가 추가되었다면 테이블을 다시 만드는 것은 기존 데이터
가 삭제되기 때문에 안됨 

ALTER TABLE 테이블이름 
ADD [CONSTRAINT 제약조건이름] 제약조건종류(컬럼이름); 

2)NOT NULL을 설정하는 것은 ADD로 안됨 
=>NOT NULL은 NULL이 가능한 것으로 수정하는 것이기때문에 add가 아니라 
MODIFY를 이용합니다. 
ALTER TABLE 테이블이름 
MODIFY [CONSTRAINT 제약조건이름] NOT NULL;
 
 3)제약조건 제거 
 ALTER TABLE 테이블이름 
DROP 제약조건이름;

4)제약조건의 활성화 비활성화 
=>테스트를 할 때 제약조건 때문에 테스트를 제대로 못하는 경우가 있어서 제약조건을 
일시적으로 중지 시켰다가 나중에 다시 활성화 할 수 있습니다. 

=> member 와 board테이블이 있을 때 board테이블에 데이터를 추가하거나 데이터를 
갱신하는 작업을 테스트하고자 하는데 외래키가 설정되어 있어서 board테이블에 데이터
를 추가하려면 회원테이블에 데이터를 추가하고 작업을 해야합니다. 
 
 ALTER TABLE 테이블이름 
 disable 제약조건이름; 

 ALTER TABLE 테이블이름 
 enable 제약조건이름; 

SELECT *
FROM user_constraints;

ALTER TABLE BOARD 
DROP CONSTRAINT SYS_C007210;

**VIEW 

보안의 기본적인 2가지 개념 
1. Authentication(인증): 로그인 
2. Authorization(인가): 권한 

=> 자주 사용하는 SELECT구문을 하나의 이름을 만들어 두었다가 마치 테이블인것 
처럼 사용하는 가상의 테이블 
=> 가상의 테이블이지만 실제 물리적인 테이블과 기능은 거의 유사 
이름은 VIEW이지만 데이터 삽입, 삭제, 갱신이 가능 
옵션을 설정하거나 2개이상의 테이블로부터 유도된 경우에만 읽기 전용 
=> 사용하는 목적은 자주 사용하는 SELECT구문을 빠르게 사용하기 위한 목적이 있고 
보안 때문입니다. 

1. 뷰를 생성하는 구문 
create [or replace] [force | noforce] view 뷰이름 
as
select 구문 
[with check option]
[with read only]

=>or replace는 뷰가 존재하면 지우고 다시 생성 
=> force는 select구문에 있는 테이블이 존재하지 않더라도 뷰를 생성 
=>with check option은 뷰에 데이터를 삽입하거나 삭제하거나 갱신할 때 뷰생성
조건을 확인 
=>with read only는 뷰 읽기전용으로 생성 
 
실습 
1.DEPT 테이블의 모든 데이터를 복사해서 DEPTCOPY테이블을 생성 
=> 기존의 테이블이 존재하면 삭제 
DROP TABLE DEPTCOPY;

create table deptcopy 
as 
select * 
from dept;
-- 확인 
SELECT *
FROM deptcopy;

2. DEPTCOPY테이블의 DEPTNO 와 DNAME컬럼만을 갖는 DEPTVIEW를 생성 
CREATE VIEW DEPTVIEW 
AS 
SELECT DEPTNO, DNAME 
FROM DEPTCOPY;

=> view의 사용법은 테이블과 동일 

3. DEPTVIEW의 내용을 조회 
SELECT * 
FROM DEPTVIEW; 
=>DEPTVIEW를 사용할 때 위에 만든 구문이 실행 
SELECT DEPTNO, DNAME
FROM DEPTCOPY; 

=>실제 테이블이 만들어 진 것이 아님 

=> 뷰에 데이터를 삽입하게 되면 원본 테이블에 데이터가 삽입됩니다. 

4. DEPTVIEW에 데이터를 삽입 
INSERT INTO DEPTVIEW(DEPTNO, DNAME)
VALUES(50,'비서'); 

5. DEPTVIEW를 조회 
SELECT * FROM deptview;

6. DEPTCOPY테이블을 조회 
SELECT * FROM deptcopy;

=>뷰는 구조변경이 안됩니다. 
ALTER VIEW라는 명령을 사용할 수 없습니다. 

=>뷰의 삭제는 가능 
DROP VIEW 라는 명령으로 삭제 

=>CREATE 다음에  OR REPLACE옵션을 추가하면 VIEW를 다시 만들 수 있습니다. 

7. DEPTVIEW를 DEPTNO와  DNAME만 갖도록 구조를 변경 

ALTER VIEW DEPTVIEW 
AS 
SELECT DEPTNO, LOC 
FROM DEPTCOPY; 
=>위의 명령은 에러 

=> 삭제하고 다시만들던가 OR REPLACE옵션 이용 

CREATE OR REPLACE DEPTVIEW 
AS
SELECT DEPTNO, LOC 
FROM DEPTCOPY;

=>서브쿼리에 존재하지 않는 테이블 이름을 기재하면 에러 
FORCE 옵션을 이용하면 생성은 가능 
실제로 테이블을 만들기 전까지는 조회나 삽입, 삭제, 갱신은 안됨 
=> 뷰는 실제 데이터가 아니고 메모리에 만들어질 때는 SELECT구문만 저장 

8. 현재 존재하지 않는 DEPTVIRTUAL테이블의 모든 데이터를 가지고 VDEPT라는 
VIEW를 생성 

CREATE OR REPLACE VIEW VDEPT 
AS
SELECT * 
FROM DEPTVIRTUAL; 
table or view does not exist 에러 
테이블이나 뷰가 없는데 사용하려고 해서 발생하는 에러 
테이블이 없는데도 만들고자 하면 FORCE옵션을 추가 

CREATE OR REPLACE FORCE VIEW VDEPT 
AS
SELECT * 
FROM DEPTVIRTUAL; 

=> 만들어는 지지만 DEPTVIRTUAL생성되기 전까지는 사용은 못함 
SELECT * 
FROM VDEPT;
=> 이 구문은 에러  

=>WITH CHECK OPTION은 VIEW에 데이터를 삽입, 삭제,갱신하려고 할 때 VIEW
의 조건을 확인할 것인지 설정하는 옵션 
이 옵션이 없으면 VIEW의 생성조건에 맞지않는데 데이터를 삽입, 삭제, 갱신할 수 
있습니다. 

10.DEPTCOPY테이블에서 DEPTNO가 30인 데이터만 가지고 DEPTVIEW 를 생성 
CREATE OR REPLACE VIEW DEPTVIEW 
AS 
SELECT * 
FROM DEPTCOPY 
WHERE DEPTNO = 30;

SELECT *
FROM DEPTVIEW;
=> 데이터가 1개만 존재 

11.DEPTVIEW 에 DEPTNO 가 70이고 DNAME인 회계인 데이터를 삽입 
INSERT INTO DEPTVIEW(DEPTNO,DNAME)
VALUES(70,'회계');

=> 확인 
SELECT * 
FROM DEPTCOPY;

12.VIEW를 생성한 조건에 맞는 경우에만 데이터 조작작업이 가능하도록 하려면 
서브쿼리 뒤에 WITH CHECK OPTION을 추가 

CREATE OR REPLACE VIEW DEPTVIEW 
AS 
SELECT * 
FROM DEPTCOPY 
WHERE DEPTNO = 30
WITH CHECK OPTION;

=> 확인 
SELECT * 
FROM DEPTVIEW;

13.DEPTVIEW 에 DEPTNO 가 80이고 DNAME인 홍보인 데이터를 삽입 

INSERT INTO DEPTVIEW(DEPTNO,DNAME)
VALUES(80,'홍보');
=>데이터가 삽입되지 않고 WITH CHECK OPTION 오류가 발생 

=> 오라클의 뷰는 테이블과 거의 유사한 기능을 수행 - 삽입, 삭제, 갱신이 가능 
=> 2개 이상의 테이블로부터 유도된 뷰는 삽입, 삭제, 갱신에 제약 
=> 뷰를 만들 때 마지막에 WITH ONLY 를 추가해주면 읽기 전용의 뷰가 됩니다. 

-- 포트폴리오에 삽입 
14. 위와 동일한 뷰를 읽기 전용으로 생성 
CREATE OR REPLACE VIEW DEPTVIEW 
AS 
SELECT * 
FROM DEPTCOPY 
WITH READ ONLY;

=> 아래처럼 데이터를 삽입하려고 하면 READ ONLY VIEW라는 에러메세지 출력 
INSERT INTO DEPTVIEW(DEPTNO, DNAME)
VALUES(99,'기획'); 

** Inline View 
=>from절에 삽입된 서브쿼리 
=> 오라클은 rownum이라고 하는 일련번호 컬럼을 select구문의 결과로 리턴 
이 컬럼은 from절에서 where절로 데이터를 가지고 올 때 이전 데이터의 
rownum에 +1한 값을 갖는데 where절의 조건을 만족하지 못하면 사라지고 다시
이전데이터의 rownum+1이 됩니다. 
=> 오라클에서 TOP - N을 구하는 것이 어려운 문제 
특정한 범위의 순위 데이터를 가져오는 것이 어렵습니다. 

MYSQL에서는 행번호를 생성하는 부분이 가장 마지막에서 이루어지기 때문에 
limit옵션을 이용해서 순위를 가지고 데이터를 추출하는 것이 쉽습니다. 

1. emp테이블의 맨앞의 데이터 5개 가져오기 
select * 
from emp
where rownum < 6;

2. 6번째 이후의 데이터 가져오기 
select * 
from emp
where rownum >= 6;
=>위의 문장을 수행하면 데이터가 한 개도 출력이 안됩니다. 
rownum은 where절을 통과하는 데이터가 있을 때 1씩 증가합니다. 

3.rownum을 이용해서 > 조건을 만들려면 where절을 수행하기 전에 rownum을 
확정해야 합니다. 
from절에서 미리 데이터를 가지고 와서 rownum을 확정해야 합니다. 
이때 from절에서 서브쿼리를 이용하게 되는데 이것을 inline view라고 합니다. 

select * 
from (select rownum r, ename, hiredate from emp)
where r >= 6;

4. 	페이징 처리를 할 때는 대부분 특정 컬럼을 기준으로 정렬한 후 처리를 합니다. 
order by 절은 select에서 가장 마지막에 수행된다는 것을 기억해야 합니다. 
hiredate가 가장 느린 5개의 데이터를 출력 
select * 
from (select rownum r, ename, hiredate from emp order by hiredate desc)
where r < 6;

MARTIN, JONES, WARD, ALLEN, SMITH 가 조회 
ADAMS, SCOTT, MILLER, FORD, JAMES가 조회 되어야 함 

=> 정렬을 하기전에 번호가 매겨지기 때문에 번호 순으로 가져온 후 정렬이 됩니다. 
서브쿼리의 from절에서 미리 정렬을 하고 번호를 매기도록 수정해 주어야 합니다. 

select * 
from (select rownum r, ename, hiredate from (
     select * from emp order by hiredate desc ))
where r < 6;

위의 쿼리를 이용해서 첫번째 3개만 출력 
나머지 전체를 출력하기 
더보기를 구현하는 원리 

select * 
from (select rownum r, ename, hiredate from (
     select * from emp order by hiredate desc ))
where rownum <  4; 

select * 
from (select rownum r, ename, hiredate from (
     select * from emp order by hiredate desc ))
where r  > 3;

한 페이지당 5개씩 보여주기 - page당 데이터의 개수는 cnt 에 저장 
i변수가 1부터 1씩 증가한다고 가정 
select * 
from (select rownum r, ename, hiredate from (
     select * from emp order by hiredate desc ))
where r > (cnt*(i-1)) and r <= cnt*i;

** Sequence
=>일련번호를 만들어주는 객체 
=> 오라클에서는 Sequence 이고 다른 데이터베이스에서는 대부분 autoincrement 
1. 생성 
create sequence 시퀀스이름 
    [start with 시작번호]
    [increment by 증가치] 
    [maxvalue 최대값]
    [minvalue 최소값]
    [cycl | nocycl -최대값에 도달한 경우 최소값을 갈 것인지 여부: 기본은 
    nocycle] 
    [cache 보관개수 | nocache - 시퀀스의 값을 메모리에 저장할 지 여부 ]   
 => 옵션없이 생성하면 관리자 계정은 0부터 시작하는데 다른 계정은 숫자가 제각각 
 =>cycle을 설정하면 이경우에는 시퀀스를 기본키로 사용하면 안됩니다. 

2.시퀀스 사용 
시퀀스이름.nextval: 다음 시퀀스 값 
시퀀스이름.currval: 현재 시퀀스 값 -nextval을 1번은 하고 호출 

3. 시퀀스 수정 
alter sequence 시퀀스이름 
    옵션설정 다시 
=> start with는 수정 못함 
=>start with를 수정할 때는 삭제하고 다시 생성 

4. 시퀀스 삭제 
drop sequence 시퀀스이름 

5. 시퀀스는 일반적으로 테이블에서 기본키 설정을 할려고 할 때 마땅한 컬럼이 없는
기본키 용도로 사용하기 위해서 생성 
데이터 삽입할 때 주로 이용

 실습 
 => 게시판 테이블을 생성하고 시퀀스를 이용해서 일련번호 삽입 
 게시판 테이블 - 이름 (동일한 이름을 사용하는 테이블이 있으면 삭제 )
 - 글번호: 기본키 - 정수(5자리) 
 - 글제목: 필수입력 - 문자열 한글 100자리까지(변하지 않음) 
 - 글내용: 문자열인데 1000자이상 저장 가능 
 - 작성일: 날짜로 만들고 기본값은 현재 시간 
 - 작성자: 필수입력 - 영문과 숫자 50자리 (변하지 않음)
 

DROP TABLE board;
--테이블 생성 
create table BOARD(
  num number(5)PRIMARY KEY,
  title varchar2(300) not null,
  content clob,
  regdate DATE DEFAULT sysdate,
  writer varchar2(50) NOT null);

-- 확인 
SELECT * 
FROM board; 

=> 1부터 시작하는 시퀀스 생성 
CREATE SEQUENCE boardseq
  start with 1; 

=> 시퀀스를 이용해서 데이터를 삽입 
insert into board(num, title, content, writer)
values(boardseq.nextval, '제목', '내용',
'관리자');

insert into board(num, title, content, writer)
values(boardseq.nextval, '하오', '중국어인사',
'사용자');
 
insert into board(num, title, content, writer)
values(boardseq.nextval, '싸바', '불어인사',
'사용자2');

ALTER SEQUENCE boardseq 
   INCREMENT BY 10;

insert into board(num, title, content, writer)
values(boardseq.nextval, '꼬모에스따', '스페인인사',
'사용자3');  

-- 확인 
SELECT * 
FROM board;










  
  
  
 
 */
}
