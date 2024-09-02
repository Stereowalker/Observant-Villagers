package com.stereowalker.obville.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.stereowalker.unionlib.config.ConfigObject;
import com.stereowalker.unionlib.config.UnionConfig;

import net.minecraftforge.fml.config.ModConfig.Type;

@UnionConfig(folder = "Obville Configs", name = "villager_lines", translatableName = "config.obville.villager_lines.file", autoReload = true, appendWithType = false)
public class ExtraLinesConfig implements ConfigObject {	

	@UnionConfig.Entry(name = "Village Leader Redemption Lines", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What the leader will say randomly after a redemption trade is complete"})
	public List<String> leader_lines = Lists.newArrayList(
			"Just try to stay out of trouble next time, you can pay back for things you did but that wont stop people from holding grudges against you or worse.",
			"Thanks, this means alot.",
			"Alright that will cover it, keep yourself out of trouble from now on alright?",
			"Thanks for doing this, I hope we don't have to do it again.",
			"You did the right thing this time, let's see if that sticks.",
			"Good. But if you mess up again, things will get worse real quick.",
			"This will cover that for now, remember, trust is hard to regain. So be careful with causing trouble, or even I won't be able to help you.",
			"This is a step in the right direction, but don't expect everyone to forgive you.",
			"Alright, now stay out of trouble or you will have to owe alot more.",
			"Alright, but your still on thin ice. We will be watching you.",
			"Your still on our watch list, be careful with what you do.",
			"Thanks, now keep trying to improve or else you will be screwed.",
			"Remember, you only get so many chances. Don't waste them.",
			"This will do, keep yourself out of trouble and you won't have anything to worry about.",
			"This mostly covers what you did, please keep yourself out of trouble. I can't help you if you cause too many issues, if you do anything worse, you will have to answer to me.",
			"Thanks, but it will take more to regain the trust of the village.",
			"This is good, one less problem now but keep yourself out of trouble you hear?",
			"That settles this issue. Don’t make me regret helping you, stay out of trouble.",
			"Consider this a warning. Keep yourself clean.",
			"This covers the damage. Just remember, we’re watching.",
			"Fine, but don’t think this means you’re off the hook. We are watching.",
			"Alright, that’s settled. But remember, we’re always watching.",
			"Thanks for this, but you’ve still got a lot to prove."
			);

	@UnionConfig.Entry(name = "Villager Distruested Lines", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a villager will say randomly if they decide they don't want to trade with distrusted players"})
	public List<String> distrusted_lines = Lists.newArrayList(
			"You think you can do what you did then get trades from me? Find someone else to trade with.",
			"I heard about what you did, I don't want trouble, please leave me alone.",
			"Why would I trade with someone like you? Screw off.",
			"I don't trust you, find someone else to bother.",
			"Please go away.",
			"I don't do deals with someone who has no respect for our Village. Bug off.",
			"Leave before you cause more trouble, everyone around here knows about what you did.",
			"Please leave me alone, I don't want to be seen talking with a thief.",
			"Stay away from me and this Village.",
			"Oh uh, I uh, think I forgot my stuff at home. I can't trade right now, please go away.",
			"Your on my list of people to avoid, stay away from me.",
			"If you need something, I'm not interested in trading with you.",
			"Why would I trust you? Get out of my face.",
			"Go trade somewhere else, you're not welcomed here after what you did.",
			"I don't feel safe around you, please go away.",
			"Don't speak to me.",
			"I don't want any trouble, please don't speak to me.",
			"I don't want any trouble. Stay far away from me please.",
			"After what you did, you think I’d trade with you? Go away.",
			"No please stay away from me, I don't want to be seen talking to someone like you.",
			"Not trading with you, go away.",
			"I don't have anything, please leave me alone.",
			"I know you. You have a terrible reputation around here, don't bother trying to talk with me. It ain't going to happen.",
			"After what you did, I can't risk trading with you.",
			"I can't be seen talking or trading with someone like you.",
			"I think I forgot to water my uh, bed. I cant trade now, please leave me alone.",
			"There’s no way I’m trading with you. People would talk and my reputation would be ruined.",
			"Get lost. I don’t deal with troublemakers.",
			"I have nothing to say to you. Leave now.",
			"Trading with you would only bring me trouble. Go away.",
			"I’ve got nothing for you. Find someone else.",
			"I know your kind. Take your business elsewhere.",
			"I don’t deal with people like you. Move along."
			);

	@UnionConfig.Entry(name = "Common Bribe Rejected Lines", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a villager will say randomly if they decide they won't take your bribe"})
	public List<String> common_bribe_fail = Lists.newArrayList(
			"What? No, I don't want your money.",
			"You think bribery will fix this? NO.",
			"I won't be silenced with emeralds.",
			"You cant bribe your way out of this.",
			"NO I dont want your dirty money. SOMEONE HELP, HELP.", //(Runs away)
			"No no no I won't be tempted by emeralds.",
			"No amount you offer will make me silent.",
			"How dare you, I won't forget what you just did just because of some emeralds.",
			"Stay away from me.",
			"No, I'm telling everyone what you did. Stay away from me.", //(Runs away)
			"I don't do bribes, you will pay for what you did! Figuratively.",
			"I don't want your damn emeralds, how dare you.",
			"You won't get away with this, no amount of emeralds will interest me.",
			"I’m not that easy, your in trouble.",
			"I am not letting you get away, no amount of emeralds will make me happy compared to what they will do to you when you're exiled from this Village.",
			"Uh, erm. No. No, I am not taking your emeralds. What you did is something I can't forgive.",
			"Do you really think you can just do this and then just bribe me? Are you insane?",
			"No, you're going to rot for this. I'm telling everyone.", //(Runs away)
			"No I won't take your emeralds mate, your in trouble. Accept reality.",
			"AAAAA STAY AWAY FROM ME.", //(Runs away)
			"Aren't those my emeralds? Are you really trying to bribe me with my own emeralds?",
			"I dont want your money, please leave me alone.",
			"No, I dont want your emeralds NO.",
			"I'm tempted but i'm better than that, I will let the entire village know of what your doing.",
			"No way, I won't be bought with emeralds.",
			"I'm not for sale!",
			"Your emeralds mean nothing to me.",
			"Your attempt to bribe me is pathetic.",
			"I don't take bribes. Period.",
			"You can't bribe your way out of this one.",
			"Your bribe means nothing to me."
			);

	@UnionConfig.Entry(name = "Semi-Rare Bribe Accepted Lines", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a villager will say randomly if they decide they take your bribe"})
	public List<String> rare_bribe_success = Lists.newArrayList(
			"Do you really think you can bribe me? Your damn right, I saw nothing.",
			"Suddenly I don't remember the last minute. Heh, bye.",
			"I saw nothing.",
			"Pleasure doing business with you.",
			"Okay deal, please leave me alone I promise i’ll forget all of this.",
			"Okay fine, just dont hurt me please i’ll forget everything.",
			"How dare you think I will forget what you did just because you offered me some emeralds? Cause your right, I never saw you. Bye.",
			"Mhm alright, I never saw you.",
			"Alright all is forgiven, I was never here.",
			"I never saw a thing.",
			"I was never here.",
			"Thanks for the emeralds, I will forget that this all happened.",
			"OOP alright I was never here.",
			"Well, if you insist... I saw nothing.",
			"Oh yeah, consider it forgotten.",
			"Okay, okay, I didn't see anything. Just go.",
			"Fine, I never saw you.",
			"Alright, it's forgotten. Now leave.",
			"Oh I suddenly don't remember why I'm here. I guess I saw nothing.",
			"Deal. I saw nothing."
			);

	@UnionConfig.Entry(name = "Caught Commiting Crime", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a villager will say randomly if they're the only one that caught you commiting a crime"})
	public List<String> caught = Lists.newArrayList(
			"HEY, what are you doing?",
			"Excuse me, is that yours?",
			"Uh, what are you doing?",
			"Excuse me, what do you think you are doing?",
			"HEY, what do you think you're doing?",
			"I won't let you get away with this, I saw that. I'm telling everyone about what you're doing",
			"HEY STOP, you can't do that. What's wrong with you?"
			);

	@UnionConfig.Entry(name = "Caught Commiting Crime By Guard Villager", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a guard villager will say randomly if they're the only one that caught you commiting a crime"})
	public List<String> guardCaught = Lists.newArrayList(
			"What are you doing? Hm? Im watching you.",
			"What do you think your doing? Do I have to be watching you from now on?",
			"Is that yours?",
			"I think you need to leave. Thieves don't last very long in our Village.",
			"Hey, what do you think your doing?",
			"HEY, what are you doing? I'm going to be watching you from now on.",
			"Wait, what are you doing?",
			"Did you just steal something? I'm watching you.",
			"I suggest you leave and don't come back. We don't welcome people who bother the village, I better not catch you doing something again.",
			"I'm going to keep my eye on you.",
			"Excuse me, what do you think your doing?",
			"I think I need to keep an eye on you for a bit.",
			"I'm watching you, be on your best behavior or else."
			);

	@UnionConfig.Entry(name = "Recovering From Distrust", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a villager will say randomly when you were distrusted and are now regaining their trust"})
	public List<String> recoverFromDistrusted = Lists.newArrayList(
			"Uh, yeah I am still not sure about trading with you. Maybe if I hear that you keep improving I'll give you a chance but until then, stay away from me please.",
			"I don't care if you sorted stuff out with one of our Villager Leaders or helped someone, I need to see some more improvement before I trade with you again.",
			"I just heard you went to atone for what you did but I need some time to get comfortable around you again, I feel uneasy just talking. How about we trade later? I am busy right now anyways.",
			"Uh, yeah…. no. Maybe if I keep hearing that your improving but this is too soon.",
			"Uh? What in the world makes you think I want to trade with you? Did something change?",
			"Is there a reason you're talking to me? Did I miss something? I don't remember anything changing between us or you becoming a better person.",
			"How about we start over another time? This is too soon, people don't move on that fast mate.",
			"Your recent actions aren't enough. Come back when you've really changed. I could care less what other people say, I need to see you change myself before I consider trading again.",
			"I've just heard you've been trying to make amends, but I'm not ready to trade with you right after you did so. It makes it seem like you think you can just get forgiveness by doing some good.",
			"Just because you did something right doesn't mean I suddenly trust you.",
			"Don't think a brief change makes me trust you, I won't be fooled.",
			"It’s too soon to consider trading with you.",
			"You might be improving yourself or whatever, but it’s not enough for me to trade with you just yet.",
			"Have you ever heard anyone say \"Forgiveness comes with time and chances come from respect.\" So let’s talk again later or another day, maybe then I'll be ready to trade.",
			"Am I missing something? Why are you trying to talk to me? Last I checked most people agreed that we wanted nothing to do with you after your crimes.",
			"Why are you talking to me? Did something change? Please stay away from me, I don't want to be seen talking with a criminal.",
			"I don't remember anything changing, I told you to leave me alone before didn’t I?",
			"Uh, excuse me, why are you trying to talk to me? I don’t remember anything changing between us.",
			"I won't trade with you just because you did something good… but i’ll give you a chance if you can go a few days without issues.",
			"Leave me alone.",
			"This is too soon, stay out of trouble for a few days and i’ll consider changing my mind.",
			"Go a few days without issues and I'll trade with you. I need to see you actually won't cause issues again, not just hear about it.",
			"Keep out of trouble and maybe I’ll consider trading with you again.",
			"Uh, yeah no, I won't be trading with you still. Maybe if you keep out of trouble then sure but I want to be cautious." ,
			"Uh… let’s give it some time. If you stay away from causing issues for awhile, then we’ll talk about trading.",
			"Excuse me? Just because you’ve done some good doesn’t mean I want to trade. I know your kind, in a day you will be causing issues again.",
			"NO. No trades. I need to see you will stay out of trouble before I even consider trading with you.",
			"No, I know your kind. People like you don't change, just like a lamb to the slaughter you are going to be causing trouble the next day. You might as well leave this village and go cut down some trees in a forest, cause you people ain’t good for nothing else. Better yet, don't come back and stay in the forest. You vine climbers should feel right at home cause at the end of the day, you are just another spoonear punk.",
			"I don't think we'll ever be trading, because I know your type— improving yourself today, causing chaos tomorrow. Prove me wrong.",
			"Woah no no, I don't want to be anywhere near you. You might say your doing better but I don't feel comfortable… crap that is kinda unfair though. Alright how about this, if I see you're not causing issues for a few days, I'll give you a chance and we can trade again.",
			"Trading? Not right now, I can't just trade with someone just because your doing better. We need to see more improvement or something, it is still too soon to just forget and go back to trading. For all I know, you will go right back to causing trouble."
			);

	@UnionConfig.Entry(name = "Distrusted Again", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a villager will say randomly when you become distrusted again"})
	public List<String> distrustedAgain = Lists.newArrayList(
			"Why would I want to trade with you? You don't know when to stop being an issue, I don't know what's with you or if you were a mistake during birth but keep away from me and my home.",
			"I’m not trading with someone like you. We gave you a chance already, stay away from me.",
			"Why in the world would I trade with you?",
			"What? You have some nerve, it will take alot to make me trade with someone like you again. Fool me once, shame on me, fool me twice…",
			"Is there a reason your trying to talk to me? Leave me alone.",
			"Uh, I don't want any trouble, uh, please just, keep away from me.",
			"We've been down this path before, and I'm not falling for it again. It would take alot for me to trade with you again.",
			"Your reputation speaks for itself, and it's not saying anything good. Get lost.",
			"You're trouble, plain and simple. Stay away from me and my business.",
			"I've heard enough about you to know I want nothing to do with you anymore. Stay away from me",
			"Why would I trade with someone who can't be trusted? Go away.",
			"I don't want any trouble. Leave and don't come back.",
			"I won't be fooled twice. Get out of my sight.",
			"You've proven you're not trustworthy. Don't bother me again.",
			"Why would I trade with someone as untrustworthy as you? Get lost.",
			"Really pretending nothing happened? You’ve messed up again…. I don’t want to see your face anymore, go, go away, anywhere but here. I can't be seen trading with you.",
			"I don't want to be associated with someone like you.",
			"I can’t trade with you anymore. I cant have people think I trade with criminals, leave me alone.",
			"Why would I trade with someone who keeps causing problems? Go away.",
			"I’m not interested in trading with someone as unreliable as you.",
			"You proved your not trustworthy now, don't trade with me. Stay away from me.",
			"After causing issues again, do you really expect me to trade with you?",
			"Your presence is not welcome anymore. Leave immediately.",
			"You’ve caused enough issues. Don’t make it worse by bothering me.",
			"I can’t risk being seen with you anymore. Go away.",
			"You’ve lost the trust of this village. Don’t expect any trades.",
			"I’m not going to deal with someone who’s caused so much trouble.",
			"You’ve ruined your chances here. Find another village.",
			"You’ve tarnished your name here at this point. No trades.",
			"You’ve made your bed. Now lie in it. I won't trade with idiots who keep causing issues."
			);

	@UnionConfig.Entry(name = "Blacklisted", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a villager will say randomly if they've blacklisted you"})
	public List<String> blacklisted = Lists.newArrayList(
			"I saw what you did, I am not trading with you.",
			"You know I saw you do a crime earlier right? Why would I ever trade with a criminal?",
			"No stay away from me, I saw the crime you did earlier and I wont be a victim, STAY AWAY FROM ME.",
			"You here to rob me? Otherwise stay away from me, I saw what you have done earlier and I want nothing to do with it.",
			"You think I'll trade with a criminal? Not a chance.",
			"Stay away. I saw what you did earlier, and I'm not interested in trading.",
			"I can't trust you after what I saw. No trades.",
			"I saw what you did. Don't come near me.",
			"I saw you doing the crime earlier. You're not getting any trades from me because I am not trying to get associated with you.",
			"You clearly dont remember me, I'm not trading with you after what I saw you did earlier. Get lost.",
			"I saw what you did earlier. Don't even think about trading with me.",
			"Please stay away from me, I saw you doing stuff earlier and I wont be accused of being part of it because I traded with you.",
			"I can't trade with someone I saw committing a crime, then I might be associated with you if you do worse.",
			"I witnessed you earlier doing a crime, I wont trade with someone like you.",
			"Oh nah, I saw what you did earlier and I want nothing to do with someone like you. It dont matter if a crime is big and cool or small and unimportant I want nothing to do with criminals."
			);
	
	@UnionConfig.Entry(name = "Invisible Crime", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a villager will say randomly if you commit a crime while you're invisible"})
	public List<String> invisible = Lists.newArrayList(
			"Huh? Is someone there?",
			"What? Huh? What just happened?",
			"Hey, is someone there?",
			"Oh... I must be hallucinating! I need to go seek help.",
			"What the... who..",
			"Is someone here? Who just did this?",
			"Huh.. what just happened?",
			"......",
			"I am going to be blamed for this aren't I?",
			"WHAT THE HECK? WHAT JUST HAPPENED?",
			"HUH? WHAT?",
			"WHAT? I need to tell the Villager Leader about this.",
			"What?",
			"Am I seeing things?",
			"Now what in tarnation..",
			"I need to report this to the Village Leaders. This is weird.",
			"Is someone invisible?",
			"Hm?",
			"..... the heck?",
			"How.. what?"
			);
	
	@UnionConfig.Entry(name = "Bounty Completed", type = Type.COMMON)
	@UnionConfig.Comment(comment = {"What a village leader will say randomly if you sibmit a bounty"})
	public List<String> bounty = Lists.newArrayList(
			"Finally someone got them! Here, take your reward for finally taking down that weirdo. Thank you.",
			"Thank you for fulfilling the bounty. Although I wish it didn't have to be like this sometimes, why are some people like that?",
			"Thanks for doing the bounty, they had it coming. Take your reward.",
			"You know I actually met the person on that bounty, if I knew then I would have killed them myself. People like that are worse than zombies.",
			"Finally someone did that bounty, it must have been hard considering we haven't been able to get them ourselves. We are grateful.",
			"Oh, you did the bounty. I was not expecting someone to actually be able to take them down, thank you. This means alot to some people, may they rest in peace… although maybe not too comfortably.",
			"Here is your reward for your bounty, although I wonder how some people get like that. What makes a person a jackwad?",
			"You know, I wish I was rich, I would put bounties on so many people myself… terrible people of course, yes.",
			"Thank you for completing the bounty, I was waiting for someone to get that vine climbing repulsive tree punching spooner. Here is your reward.",
			"Good job on completing the bounty, having less of someone like that in the world is a step forward to a safer home. I want to be able to do things like party without worrying if someone is going to randomly get kidnapped and become a slave.",
			"You did well, you put things right by fulfilling this bounty. I just wish I could save my people more, I don't know if I can handle seeing someone I've known for a long time being taken away from all of us again, it's too much.",
			"Here is your reward! Keep it up.",
			"This is your reward, thanks for putting them down."
			);
}
