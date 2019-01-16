package net.benjaminurquhart.jch;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command<T> {

	private String name;
	private String[] args;
	
	private CommandHandler<T> handler;
	
	public Command(){
		this(null);
	}
	public Command(String name, String... args){
		if(name == null){
			name = this.getClass().getSimpleName().toLowerCase();
		}
		this.name = name;
		this.args = args;
	}
	public String getHelpMenu(){
		return Usage.getUsage(handler, this, args);
	}
	public String getName(){
		return this.name;
	}
	public String getDescription(){
		return "no description provided";
	}
	public boolean hide(){
		return false;
	}
	public String[] getAliases() {
		return new String[0];
	}
	protected void setHandler(CommandHandler<T> handler){
		this.handler = handler;
	}
	public abstract void handle(GuildMessageReceivedEvent event, T self);
}
