package grails.plugin.feature.toggle.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Feature {
  String name() default NULL;
  String[] with() default {};
  String[] without() default {};
	int responseStatus() default 404;
	String responseRedirect() default "";
  public static final String NULL = "NULL Value for feature toggle"; //Java bizarrely does not allow null for annotations
}
