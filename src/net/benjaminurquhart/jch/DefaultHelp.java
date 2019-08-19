package net.benjaminurquhart.jch;

import java.util.List;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class DefaultHelp<T> extends Command<T> {
	
	@Override
	public void handle(GuildMessageReceivedEvent event, T self) {
		TextChannel channel = event.getChannel();
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
