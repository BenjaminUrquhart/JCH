package net.benjaminurquhart.jch;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;

public class CommandEvent {

	private JDA jda;
	private String user;
	private String guild;
	private String channel;
	private Message message;
	
	private String messageString;
	
	private MessageReceivedEvent msgEvent;
	private SlashCommandInteractionEvent slashCommandEvent;
	
	protected CommandEvent(MessageReceivedEvent event) {
		this.jda = event.getJDA();
		this.message = event.getMessage();
		this.channel = event.getChannel().getId();
		this.messageString = message.getContentRaw();
		
		this.user = event.getAuthor().getId();
		
		if(event.isFromGuild()) {
			this.guild = event.getGuild().getId();
		}
		
		this.msgEvent = event;
	}
	
	protected CommandEvent(SlashCommandInteractionEvent event) {
		this.slashCommandEvent = event;
		
		this.jda = event.getJDA();
		this.user = event.getUser().getId();
		this.guild  = event.getGuild().getId();
		this.channel = event.getChannel().getId();
		this.messageString = event.getCommandPath();
	}
	
	public boolean isSlashCommand() {
		return slashCommandEvent != null;
	}
	
	public JDA getJDA() {
		return jda;
	}
	
	public User getUser() {
		User out = jda.getUserById(user);
		if(out == null) {
			return jda.retrieveUserById(user).complete();
		}
		return out;
	}
	
	public Guild getGuild() {
		if(guild == null) {
			return null;
		}
		return jda.getGuildById(guild);
	}
	
	public TextChannel getChannel() {
		return jda.getTextChannelById(channel);
	}
	
	public Message getMessage() {
		return message;
	}
	
	public String getMessageText() {
		return messageString;
	}
	
	public MessageReceivedEvent getMessageEvent() {
		return msgEvent;
	}
	
	public SlashCommandInteractionEvent getSlashCommandEvent() {
		return slashCommandEvent;
	}
	
	public CommandReply startReply() {
		return new CommandReply(this, this.isSlashCommand() ? slashCommandEvent.deferReply() : new MessageCreateActionImpl(msgEvent.getChannel()));
	}
}
