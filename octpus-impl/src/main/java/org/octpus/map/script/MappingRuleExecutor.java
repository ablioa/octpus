package org.octpus.map.script;

import org.springframework.util.StringUtils;

public class MappingRuleExecutor {
    public static Object execute(String script, Object input) {
        String name = "xxx";
        Object result = null;
        if (StringUtils.hasText(script)) {
            try {
                result = new GroovyEvaluator()
                        .addImport("import org.octpus.map.utils.ModelHelper")
                        .addImport("import org.octpus.map.utils.SpringContextHolder")
                        .addImport("import com.cpic.*")
                        .setVariable("name", name)
                        .setVariable("input", input)
                        .evaluate("{name->"
                                + script
                                + "}(name)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
