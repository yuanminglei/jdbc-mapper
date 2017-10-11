package org.yml.jdbc.mapper.demo.entity;

import org.yml.jdbc.mapper.Mapper;
import org.yml.jdbc.mapper.PageHelper;
import org.yml.jdbc.mapper.annotation.Join;
import org.yml.jdbc.mapper.annotation.JoinBean;
import org.yml.jdbc.mapper.sql.Term;

import java.util.List;

/**
 * Created by yml on 2017/9/2.
 */
@JoinBean()
public class Test {

    private A a;
    @Join(onEntity=A.class,onColumn = "bid",byColumn="id")
    private B b;
    @Join(onEntity=A.class,onColumn = "cid",byColumn="id")
    private C c;
    private Mapper mapper;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }


    public void aa(){
        Term<AppMessage> term = Term.select(AppMessage.class);
        try {
            PageHelper<AppMessage> a= mapper.list(term,1,1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[]args){
//        Test t = new Test();
//        SqlBean sqlBean = new SqlBean(t.getClass());
        try {
            Term term = Term.select(AppMessage.class)
                    .andEqualTo("type", 1).andLessThan("id",11);
            term.or()
                    .andEqualTo("type", 0).andEqualTo("uid", 2).andLessThan("id",11);

            term.orderByDesc("id").limit(20);
            term.groupBy("id");
            System.out.println(term.getSql().getSql());

//            A a = new A();
//            a.setBid(3);
//            a.setName("测试");
//            Sql s= Term.update(a).andEqualTo("cid",1).getUpdateSql();
//            System.out.println(s.getSql());
//            for(Object p:s.getParams()){
//                System.out.print(p);
//                System.out.print(", ");
//            }
//            SaveHolder holder = SaveHolder.build(a);
//            System.out.println(holder.getInsertSql());
//            for(Object p:holder.getColumnValue()){
//                System.out.print(p);
//                System.out.print(", ");
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
