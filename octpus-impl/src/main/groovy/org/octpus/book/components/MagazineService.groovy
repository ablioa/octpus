//package org.octpus.book.components
//
//import lombok.extern.slf4j.Slf4j
//import org.springframework.stereotype.Component
//
//@Component
//@Slf4j
//class MagazineService {
//
//    def get_query_sql(String bookName){
//        def msql = "select book_code,book_name from book where opstatus!='05' and (attribute='02' or attribute='03') "
//        def items = bookName.split(" ")
//        for(int ix = 0; ix < items.length; ix ++){
//            def tup =items[ix]
//            if("&".equals(tup)){
//                continue
//            }
//
//            if(tup.contains("\'")){
//                def tp1=items[ix].replace("\'","\\\'");
//                def tp2=items[ix].replace("\'","");
//                def tp3=items[ix].replace("\'","_");
//                def tp4=items[ix].replace("\'"," ");
//                msql = msql + String.format(" and ( %s or %s or %s or %s ) ",
//                        "book_name like '%" + tp1 +"%'",
//                        "book_name like '%" + tp2+"%'",
//                        "book_name like '%" + tp3+"%'",
//                        "book_name like '%" + tp4+"%'"
//                )
//            }else{
//                msql = msql + "and book_name like '%" +tup+ "%' ";
//            }
//        }
//
//        msql = msql + "order by book_name";
//
//        return msql;
//    }
//
//    // 输出杂志情况
//    def foutput(String fname,PrintStream printStream){
//        def msql  = get_query_sql(fname)
//
//        def maplist = jdbcTemplate.queryForList(msql)
//        maplist.each{
//            printStream.printf("%s:%s\n",it.book_code,it.book_name)
//        }
//    }
//
//    /**
//     * 取所有杂志信
//     * @param magCode
//     * @param printStream
//     * @return
//     */
//    def get_manazine_meta(String magCode,PrintStream printStream){
//        def msql="select magazine_code,magazine_name from magazine_meta where magazine_code='"+magCode+"'"
//
//        def maplist = jdbcTemplate.queryForList(msql)
//        maplist.each{
//            foutput(it.magazine_name,printStream)
//        }
//    }
//}
