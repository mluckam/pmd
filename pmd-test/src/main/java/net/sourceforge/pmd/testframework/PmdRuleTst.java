/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.testframework;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import org.junit.runner.RunWith;

import com.google.common.base.CaseFormat;

import net.sourceforge.pmd.Rule;

@RunWith(PMDTestRunner.class)
public class PmdRuleTst extends RuleTst {

    @Override
    protected void setUp() {
        // empty, nothing to do
    }

    @Override
    protected List<Rule> getRules() {
        String[] packages = getClass().getPackage().getName().split("\\.");
        String categoryName = packages[packages.length - 1];
        String language = packages[packages.length - 3];
        String rulesetXml = "category/" + language + "/" + categoryName + ".xml";

        Rule rule = findRule(rulesetXml, getClass().getSimpleName().replaceFirst("Test$", ""));
        hasRuleConstant(rule);

        return Collections.singletonList(rule);
    }

    private boolean hasRuleConstant(Rule rule) {
        String ruleConstantName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, rule.getName());
        String[] packages = rule.getClass().getPackage().getName().split("\\.");
        String categoryName = packages[packages.length - 1];
        String clazzString = rule.getClass().getPackage().getName() + "."
                + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, categoryName + "RuleConstants");

        try {
            Class<?> clazz = Class.forName(clazzString);
            clazz.getField(ruleConstantName);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            fail(String.format("Class '%s' does not contain for constant '%s' for rule '%s'", clazzString,
                    ruleConstantName, rule.getName()));
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
