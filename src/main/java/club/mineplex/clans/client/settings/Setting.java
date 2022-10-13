package club.mineplex.clans.client.settings;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Setting {

    String key();

    SettingCategory category();

    boolean locked() default false;

    String displayFormat() default "%s";

}
