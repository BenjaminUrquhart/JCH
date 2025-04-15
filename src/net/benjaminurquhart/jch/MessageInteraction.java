package net.benjaminurquhart.jch;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.dv8tion.jda.api.interactions.InteractionContextType;

@Retention(RetentionPolicy.RUNTIME)
public @interface MessageInteraction {

	// Where this interaction can be used. Defaults to GUILD.
	InteractionContextType[] value() default { InteractionContextType.GUILD };
}
