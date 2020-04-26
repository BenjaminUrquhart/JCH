package net.benjaminurquhart.jch;


public class Usage {

	public static String getUsage(CommandHandler<?> handler, Command<?> command, String... args) {
		String out = "Usage: " + handler.getPrefix() + command.getName() + " ";
		if(args.length == 0) {
			return out;
		}
		for(int i = 0; i < args.length; i++) {
			out += "<%s> ";
		}
		return String.format(out.trim(), (Object[])args);
	}
}
