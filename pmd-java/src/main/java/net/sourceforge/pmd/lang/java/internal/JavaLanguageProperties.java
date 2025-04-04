/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.internal;

import org.apache.commons.lang3.EnumUtils;

import net.sourceforge.pmd.cpd.CpdLanguageProperties;
import net.sourceforge.pmd.lang.JvmLanguagePropertyBundle;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

/**
 * @author Clément Fournier
 */
public class JavaLanguageProperties extends JvmLanguagePropertyBundle {

    /**
     * @since 7.0.0
     */
    static final PropertyDescriptor<InferenceLoggingVerbosity> INTERNAL_INFERENCE_LOGGING_VERBOSITY =
        PropertyFactory.enumProperty("xTypeInferenceLogging",
                                     EnumUtils.getEnumMap(InferenceLoggingVerbosity.class))
                       .desc("Verbosity of the type inference logging")
                       .defaultValue(InferenceLoggingVerbosity.DISABLED)
                       .build();


    /**
     * @since 7.5.0
     */
    static final PropertyDescriptor<Boolean> INTERNAL_DO_STRICT_TYPERES =
        PropertyFactory.booleanProperty("xStrictTypeRes")
                       .desc("Whether to perform type resolution strictly at the start of execution or not")
                       .defaultValue(true)
                       .build();

    /**
     * @since 7.12.0
     */
    public static final PropertyDescriptor<Boolean> FIRST_CLASS_LOMBOK =
        PropertyFactory.booleanProperty("lombok")
                       .desc("Whether to consider lombok-specific things in core facilities like type inference. "
                                 + "Disable this option if you want to analyze the AST as it would appear before the lombok pre-processing is applied. "
                                 + "For instance, with this option enabled, variables declared with type lombok.val will have their type inferred based on the right-hand-side. "
                                 + "With the option disabled, the variable will have type lombok.val instead. "
                                 + "See https://github.com/pmd/pmd/issues/3119")
                       .defaultValue(true)
                       .build();

    public JavaLanguageProperties() {
        super(JavaLanguageModule.getInstance());
        definePropertyDescriptor(INTERNAL_INFERENCE_LOGGING_VERBOSITY);
        definePropertyDescriptor(INTERNAL_DO_STRICT_TYPERES);
        definePropertyDescriptor(FIRST_CLASS_LOMBOK);
        definePropertyDescriptor(CpdLanguageProperties.CPD_IGNORE_METADATA);
        definePropertyDescriptor(CpdLanguageProperties.CPD_ANONYMIZE_IDENTIFIERS);
        definePropertyDescriptor(CpdLanguageProperties.CPD_ANONYMIZE_LITERALS);
    }

    public static boolean isPreviewEnabled(LanguageVersion version) {
        return version.getVersion().endsWith("-preview");
    }

    public static int getInternalJdkVersion(LanguageVersion version) {
        // Todo that's ugly..
        String verString = version.getVersion();
        if (isPreviewEnabled(version)) {
            verString = verString.substring(0, verString.length() - "-preview".length());
        }
        if (verString.startsWith("1.")) {
            verString = verString.substring(2);
        }

        return Integer.parseInt(verString);
    }

    public enum InferenceLoggingVerbosity {
        DISABLED, SIMPLE, VERBOSE
    }
}
