package net.benjaminurquhart.jch;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MessageInteraction {

	// Whether the interaction is locked to a guild or not
	boolean value() default true;
}
