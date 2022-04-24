package org.octpus.map.script;

import groovy.lang.Binding;
import groovy.lang.Script;
import org.springframework.util.StringUtils;

import java.util.*;

public class GroovyEvaluator {
    private static GroovyScriptCachingBuilder groovyScriptCachingBuilder = new GroovyScriptCachingBuilder();
    private Map<String, Object> variables = new HashMap<>();
    private List<String> imports = new ArrayList<>();
    private static final String LINE_BREAK = "\r\n";

    public GroovyEvaluator() {
        this(Collections.emptyMap());
    }

    public GroovyEvaluator(final Map<String, Object> contextVariables) {
        variables.putAll(contextVariables);
    }

    public GroovyEvaluator setVariables(final Map<String, Object> answers) {
        variables.putAll(answers);
        return this;
    }

    public GroovyEvaluator setVariable(final String name, final Object value) {
        variables.put(name, value);
        return this;
    }

    public GroovyEvaluator addImport(final String importStr){
        imports.add(importStr);
        return this;
    }

    public GroovyEvaluator addImports(final List<String> importStrs){
        imports.addAll(importStrs);
        return this;
    }

    /**
     * 执行
     *
     * @param expression
     * @return
     */
    public Object evaluate(String expression) {
        final Binding binding = new Binding();
        for (Map.Entry<String, Object> varEntry : variables.entrySet()) {
            binding.setProperty(varEntry.getKey(), varEntry.getValue());
        }

        String importStr = StringUtils.collectionToDelimitedString(imports, LINE_BREAK);
        Script script = groovyScriptCachingBuilder.getScript(importStr + LINE_BREAK + expression);
        synchronized (script) {
            script.setBinding(binding);
            return script.run();
        }
    }
}
