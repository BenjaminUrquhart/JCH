package net.benjaminurquhart.jch;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommand {

	// Whether the command is still usable via normal messages
	boolean value() default true;
}
