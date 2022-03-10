package org.octpus.map.utils;

import org.springframework.util.StringUtils;

public class FactorScriptExecutor {
    public static Object execute(String script,Object input) {
        String name="xxx";
        Object result = null;
        if (StringUtils.hasText(script)) {
            try {
                result = new GroovyEvaluator()
//                        //因子约束级别
//                        .setVariable("constrainLevel", featureBO.getConstraintLevel())
//                        //当前要处理保单
//                        .setVariable("policy", context.getPolicyData())
//                        //基本信息
                        .setVariable("name", name)
                        .setVariable("input", input)
//                        //产品特性信息
//                        .setVariable("productFeatureInfo", context.getPolicyData().getProductFeatureInfo())
//                        //当前要处理标的分组，根据产品特性获取对应因子值时为空。
//                        .setVariable("subjectGroup", context.getSubjectGroup())
//                        .setVariable("subjectWrapper",context.getSubjectWrapper())
//                        //当前处理的责任记录，根据产品特性、标的获取对应因子值时为空。
//                        .setVariable("coverageInfo", context.getCoverageInfo())
//                        .setVariable("dataMap", context.getDataMap())
//                        .setVariable("nodeData",nodeData)
                        //导入相关工具类
//                        .addImport("import com.cpic.nonauto.runtime.premium.caculate.utils.*")
//                        .addImport("import com.cpic.nonauto.runtime.premium.caculate.utils.*")
//                        .addImport("import com.cpic.nonauto.runtime.specification.subject.wrapper.*")
                        //执行脚本
                        //.evaluate(fetchRule);
                        //改为使用闭包处理，可以提高执行效率
                        .evaluate("{name->"
                                +script
                                +"}(name)");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return result;
    }
}
