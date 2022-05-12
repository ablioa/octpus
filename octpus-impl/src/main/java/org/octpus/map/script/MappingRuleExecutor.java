package org.octpus.map.script;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class MappingRuleExecutor {
    public static Object execute(ConversionContext context,String script, Object input) {
        String name = "input";
        Object result = null;
        if (StringUtils.hasText(script)) {
            try {
                result = new GroovyEvaluator()
                        .addImport("import org.octpus.map.utils.ModelHelper")
                        .addImport("import org.octpus.map.utils.SpringContextHolder")
                        .addImport("import org.octpus.inspect.core.NodeDescriptor")
                        .addImport("import com.cpic.*")
                        .setVariable("source", context.getSource())
                        .setVariable("target", context.getTarget())
                        .setVariable("input",input)
                        .evaluate("{input->" + script + "}(input)");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("转换脚本执行错误: {}",e.getMessage());
            }
        }

        return result;
    }
}
