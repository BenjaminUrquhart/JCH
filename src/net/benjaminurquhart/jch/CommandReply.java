package net.benjaminurquhart.jch;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.FluentRestAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateRequest;

public class CommandReply implements FluentRestAction<Object, CommandReply>, MessageCreateRequest<CommandReply> {

	private CommandEvent event;
	private MessageCreateRequest<?> request;
	
	protected CommandReply(CommandEvent event, MessageCreateRequest<?> request) {
		this.request = request;
		this.event = event;
	}
	
	public CommandEvent getEvent() {
		return event;
	}
	
	@Override
	public JDA getJDA() {
		return ((FluentRestAction<?,?>)request).getJDA();
	}

	@Override
	public void queue(Consumer<? super Object> success, Consumer<? super Throwable> failure) {
		((FluentRestAction<?,?>)request).queue(success, failure);
	}

	@Override
	public Object complete(boolean shouldQueue) throws RateLimitedException {
		return ((FluentRestAction<?,?>)request).complete(shouldQueue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompletableFuture<Object> submit(boolean shouldQueue) {
		return ((FluentRestAction<Object, CommandReply>)request).submit(shouldQueue);
	}

	@Override
	public CommandReply setContent(String content) {
		request.setContent(content);
		return this;
	}

	@Override
	public CommandReply setEmbeds(Collection<? extends MessageEmbed> embeds) {
		request.setEmbeds(embeds);
		return this;
	}

	@Override
	public CommandReply setComponents(Collection<? extends LayoutComponent> components) {
		request.setComponents(components);
		return this;
	}

	@Override
	public CommandReply setSuppressEmbeds(boolean suppress) {
		request.setSuppressEmbeds(suppress);
		return this;
	}

	@Override
	public CommandReply setFiles(Collection<? extends FileUpload> files) {
		request.setFiles(files);
		return this;
	}

	@Override
	public CommandReply mentionRepliedUser(boolean mention) {
		request.mentionRepliedUser(mention);
		return this;
	}

	@Override
	public CommandReply setAllowedMentions(Collection<MentionType> allowedMentions) {
		request.setAllowedMentions(allowedMentions);
		return this;
	}

	@Override
	public CommandReply mention(Collection<? extends IMentionable> mentions) {
		request.mention(mentions);
		return this;
	}

	@Override
	public CommandReply mentionUsers(Collection<String> userIds) {
		request.mentionUsers(userIds);
		return this;
	}

	@Override
	public CommandReply mentionRoles(Collection<String> roleIds) {
		request.mentionRoles(roleIds);
		return this;
	}

	@Override
	public String getContent() {
		return request.getContent();
	}

	@Override
	public List<MessageEmbed> getEmbeds() {
		return request.getEmbeds();
	}

	@Override
	public List<LayoutComponent> getComponents() {
		return request.getComponents();
	}

	@Override
	public boolean isSuppressEmbeds() {
		return request.isSuppressEmbeds();
	}

	@Override
	public Set<String> getMentionedUsers() {
		return request.getMentionedUsers();
	}

	@Override
	public Set<String> getMentionedRoles() {
		return request.getMentionedRoles();
	}

	@Override
	public EnumSet<MentionType> getAllowedMentions() {
		return request.getAllowedMentions();
	}

	@Override
	public boolean isMentionRepliedUser() {
		return request.isMentionRepliedUser();
	}

	@Override
	public CommandReply addContent(String content) {
		request.addContent(content);
		return this;
	}

	@Override
	public CommandReply addEmbeds(Collection<? extends MessageEmbed> embeds) {
		request.addEmbeds(embeds);
		return this;
	}

	@Override
	public CommandReply addComponents(Collection<? extends LayoutComponent> components) {
		request.addComponents(components);
		return this;
	}

	@Override
	public CommandReply addFiles(Collection<? extends FileUpload> files) {
		request.addFiles(files);
		return this;
	}

	@Override
	public List<FileUpload> getAttachments() {
		return request.getAttachments();
	}

	@Override
	public CommandReply setTTS(boolean tts) {
		request.setTTS(tts);
		return this;
	}

	@Override
	public CommandReply setCheck(BooleanSupplier checks) {
		((FluentRestAction<?,?>)request).setCheck(checks);
		return this;
	}

}
