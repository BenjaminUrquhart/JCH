package net.benjaminurquhart.jch;

import java.util.List;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DefaultHelp<T> extends Command<T> {
	
	public DefaultHelp() {
		super("help");
	}
	
	@Override
	public void handle(MessageReceivedEvent event, T self) {
		TextChannel channel = event.getTextChannel();
		List<Command<T>> commands = this.getHandler().getRegisteredCommands();
		String out = "```";
		for(Command<?> command : commands){
			if(command.hide()){
				continue;
			}
			out += command.getHelpMenu().replace("Usage:", "").trim() + " - " + command.getDescription() + "\n";
		}
		out += "```";
		channel.sendMessage(out).queue();
	}
	@Override
	public String getDescription() {
		return "does all the work";
	}

}
