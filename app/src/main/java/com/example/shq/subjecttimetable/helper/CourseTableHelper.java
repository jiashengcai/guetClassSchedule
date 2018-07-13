package com.example.shq.subjecttimetable.helper;

import android.widget.ListView;

import com.example.shq.subjecttimetable.other.Exam;
import com.example.shq.subjecttimetable.other.MySubjectBean;
import com.example.shq.subjecttimetable.other.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shq on 2018/6/12.
 */

public class CourseTableHelper {



    /**
     * 获取字符串中所有数字组成一个int
     *
     * @param str
     * @return int
     */
    public static int getNumberFromString(String str) {
        str=str.trim();
        String strTemp="";
        if(str != null && !"".equals(str)){
            for(int i=0;i<str.length();i++){
                if(str.charAt(i)>=48 && str.charAt(i)<=57){
                    strTemp+=str.charAt(i);
                }
            }

        }
        return Integer.parseInt(strTemp);
    }

    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     * @param str
     * @param strStart
     * @param strEnd
     * @return String
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }


    /**
     * 正则表达式匹配String返回匹配数组
     *
     * @param str
     * @param regex
     * @return ArrayList<String>
     */
    public static ArrayList<String> getAllSatisfyStr(String str, String regex) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ArrayList<String> allSatisfyStr = new ArrayList<>();
        if (regex == null || regex.isEmpty()) {
            allSatisfyStr.add(str);
            return allSatisfyStr;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            allSatisfyStr.add(matcher.group());
        }
        return allSatisfyStr;
    }

    /**
     *解析课程表页面html对应的String
     * @param str
     * @return ArrayList<String>
     */
    public static List<MySubjectBean> htmlStringToMySubjectBeanList(String str){
        //实验课list
        ArrayList<String> listExp=getAllSatisfyStr(str,
                "<tr><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td></tr>");

        //课程对应教师list
        ArrayList<String> listTea=getAllSatisfyStr(str,
                " <td colspan=7>(.*?)</td>");
        listTea=getAllSatisfyStr(listTea.get(0),
                ">(.*?)<");

        //课程对应教师String[]
        String[] strTea=subString(listTea.get(0),">","<").split("；");


        //正课list
        ArrayList<String> listCou=getAllSatisfyStr(str,
                "<td align='center'>.*?</td>");

        //课程lsit
        List<MySubjectBean> subjectBeans = new ArrayList<>();


        //region 处理正课添加到课程list
        for (int j=0;j<listCou.size();j++) {


            //region 拆解课程list[j],判断是否是有课
            ArrayList<String> temp=getAllSatisfyStr(
                    listCou.get(j),
                    ">.*?<"
            );
            if(temp.size()==1)
                continue;
            //endregion


            //region 处理正课添加到课程list
            for(int i=0;i<temp.size();i+=3){

                MySubjectBean subjectBeanTemp=new MySubjectBean();
                subjectBeanTemp.setName(subString(temp.get(i),">","<"));//设置课程名
                subjectBeanTemp.setRoom(subString(temp.get(i+1),")","<"));//设置上课教室

                for(String sT:strTea){//设置教师
                    String[] sTTemp=sT.split(":");
                    if(sTTemp[0].compareTo(subjectBeanTemp.getName())==0){
                        subjectBeanTemp.setTeacher(sTTemp[1]);
                    }
                }
                subjectBeanTemp.setCouNumber(subString(temp.get(i+2),"：","<"));//设置课号


                //region 设置上课周数
                String[] sWeek=subString(temp.get(i+1),"(",")").split("-");

                int weekStart=Integer.parseInt(sWeek[0]);
                int weekEnd=Integer.parseInt(sWeek[1]);
                List<Integer> weekList=new ArrayList<>();
                for(int k=weekStart;k<=weekEnd;k++){
                    weekList.add(k);
                }
                subjectBeanTemp.setWeekList(weekList);
                // endregion

                //region 设置节次
                if(0<=j&&j<7){
                    subjectBeanTemp.setStart(1);
                    subjectBeanTemp.setStep(2);
                }else if(7<=j&&j<14){
                    subjectBeanTemp.setStart(3);
                    subjectBeanTemp.setStep(2);
                }else if(14<=j&&j<21){
                    subjectBeanTemp.setStart(5);
                    subjectBeanTemp.setStep(2);
                }else if(21<=j&&j<28){
                    subjectBeanTemp.setStart(7);
                    subjectBeanTemp.setStep(2);
                }else if(28<=j&&j<35){
                    subjectBeanTemp.setStart(9);
                    subjectBeanTemp.setStep(2);
                }
                //endregion


                //region 设置星期
                int day=j%7;
                if(day==0){
                    if(j==0)
                        day=0;
                }
                subjectBeanTemp.setDay(day+1);
                //endregion

                subjectBeans.add(subjectBeanTemp);

            }
            //endregion
        }
        //endregion



        //region 处理实验课添加到list
        for(int i=0;i<listExp.size();i++){

            //region 拆解实验课list[j],判断是否是有课
            ArrayList<String> temp=getAllSatisfyStr(
                    listExp.get(i),
                    "<td align=center>.*?</td>"
            );
            //endregion

            MySubjectBean subjectBeanTemp=new MySubjectBean();

            //设置课程名
            subjectBeanTemp.setName(subString(temp.get(0),"<td align=center>","</td>")
                    +"["+subString(temp.get(1),"<td align=center>","</td>")+"]");

            //设置上课教室
            subjectBeanTemp.setRoom(subString(temp.get(4),"<td align=center>","</td>"));

            //分割上课时间字符串
            String[] strArr=temp.get(3).split(",");


            //region 设置上课星期
            switch (strArr[1]){
                case "星期一":
                    subjectBeanTemp.setDay(1);
                    break;
                case "星期二":
                    subjectBeanTemp.setDay(2);
                    break;
                case "星期三":
                    subjectBeanTemp.setDay(3);
                    break;
                case "星期四":
                    subjectBeanTemp.setDay(4);
                    break;
                case "星期五":
                    subjectBeanTemp.setDay(5);
                    break;
                case "星期六":
                    subjectBeanTemp.setDay(6);
                    break;
                case "星期日":
                    subjectBeanTemp.setDay(7);
                    break;
            }
            //endregion

            //设置上课周次
            List<Integer> weekList=new ArrayList<>();
            weekList.add(getNumberFromString(strArr[0]));
            subjectBeanTemp.setWeekList(weekList);

            //region 设置上课节次
            int k=strArr[2].indexOf("、");
            if(k==-1){
                k=getNumberFromString(strArr[2]);
                switch (k){
                    case 1:
                        subjectBeanTemp.setStart(1);
                        subjectBeanTemp.setStep(2);
                        break;
                    case 2:
                        subjectBeanTemp.setStart(3);
                        subjectBeanTemp.setStep(2);
                        break;
                    case 3:
                        subjectBeanTemp.setStart(5);
                        subjectBeanTemp.setStep(2);
                        break;
                    case 4:
                        subjectBeanTemp.setStart(7);
                        subjectBeanTemp.setStep(2);
                        break;
                    case 5:
                        subjectBeanTemp.setStart(9);
                        subjectBeanTemp.setStep(2);
                        break;
                    case 6:
                        subjectBeanTemp.setStart(11);
                        subjectBeanTemp.setStep(2);
                        break;
                }

            }else {
                String[] strNumber=strArr[2].split("、");

                k=getNumberFromString(strNumber[0]);
                switch (k){
                    case 1:
                        subjectBeanTemp.setStart(1);
                        subjectBeanTemp.setStep(4);
                        break;
                    case 2:
                        subjectBeanTemp.setStart(3);
                        subjectBeanTemp.setStep(4);
                        break;
                    case 3:
                        subjectBeanTemp.setStart(5);
                        subjectBeanTemp.setStep(4);
                        break;
                    case 4:
                        subjectBeanTemp.setStart(7);
                        subjectBeanTemp.setStep(4);
                        break;
                    case 5:
                        subjectBeanTemp.setStart(9);
                        subjectBeanTemp.setStep(4);
                        break;
                    case 6:
                        subjectBeanTemp.setStart(11);
                        subjectBeanTemp.setStep(4);
                        break;
                }
            }
            //endregion

            subjectBeans.add(subjectBeanTemp);
        }
        //endregion


        return subjectBeans;
    }

    public static List<Exam> htmlToExamList(String htmlStr){
        List<Exam> examList=new ArrayList<>();
        ArrayList<String> listExamStr= getAllSatisfyStr(htmlStr,
                "<tr><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td></tr>");

        for(int i=0;i<listExamStr.size();i++){
            ArrayList<String> temp=getAllSatisfyStr(listExamStr.get(i),
                    "<td align=center>(.*?)</td>");
            Exam exam=new Exam();
            for (int j=0;j<temp.size();j++){
                String strTemp=subString(temp.get(j),">","</");
                switch (j)
                {
                    case 0:
                        exam.setCourseName(strTemp);
                        break;
                    case 1:
                        exam.setCourseNumber(strTemp);
                        break;
                    case 2:
                        exam.setWeek(Integer.parseInt(strTemp));
                        break;
                    case 3:
                        exam.setDay(Integer.parseInt(strTemp));
                        break;
                    case 4:
                        exam.setTime(strTemp);
                        break;
                    case 5:
                        exam.setRoom(strTemp);
                        break;
                }
            }
            examList.add(exam);
        }
        return examList;
    }

    public static List<Score> htmlToScoreList(String htmlStr){
        List<Score> scoreList=new ArrayList<>();
        ArrayList<String> listScoreStr= getAllSatisfyStr(htmlStr,
                "<tr><td align=\"center\">(.*?)</td><td align=\"center\">(.*?)</td><td align=\"center\">(.*?)</td><td align=\"center\">(.*?)</td><td align=\"center\">(.*?)</td><td align=\"center\">(.*?)</td></tr>");
//<tr><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td><td align=center>(.*?)</td></tr>
        for(int i=0;i<listScoreStr.size();i++){
            ArrayList<String> temp=getAllSatisfyStr(listScoreStr.get(i),
                    "<td align=center>(.*?)</td>");
            Score score=new Score();
            for (int j=0;j<temp.size();j++){
                String strTemp=subString(temp.get(j),">","</");
                switch (j)
                {
                    case 0:
                        break;
                    case 1:
                        score.setcName(strTemp);
                        break;
                    case 2:
                        break;
                    case 3:
                        score.setScore(strTemp);
                        break;
                    case 4:
                        score.setCredit(strTemp);
                        break;
                    case 5:
                        break;
                }
            }
            scoreList.add(score);

        }
        return scoreList;
    }

    public static void sort(List<Integer> list) {

        int size = list.size();

        int[] store = new int[size];

        for (int i = 0; i < size; i++) {

            store[i] = list.get(i);

        }

        for (int i = 0; i < size; i++) {   //对数据进行从小到大排序

            for (int j = i; j <size; j++) {

                if (store[i] > store[j]) {

                    int temp = store[j];

                    store[j] = store[i];

                    store[i] = temp;

                }

            }

        }

        for(int i  = 0 ; i <size ; i++)

        {

            list.set(i, store[i]);

        }

    }


}
