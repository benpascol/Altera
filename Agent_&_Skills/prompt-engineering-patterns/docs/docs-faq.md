# Frequently Asked Questions

## General Questions

### Q: What exactly are steering files?
**A:** Specialized markdown documents that teach Kiro how to think and respond. Load one in a chat, and Kiro operates through that expert framework for the entire conversation. They encode best practices and thinking patterns from senior engineers.

---

### Q: Why should I use these instead of just asking ChatGPT questions?
**A:** Because they're not generic advice. Each steering file encodes expert thinking in a specific domain. With regular AI, you get generic responses. With steering files, you get consistent, expert-level thinking throughout an entire project or conversation.

**Real difference:**
- ChatGPT: "Here's how to test" (generic recipe)
- Steering file: "Here's how to think strategically about testing" (framework)

---

### Q: Can I use steering files without Kiro?
**A:** Yes, but they work best with Kiro. You can read them as reference guides, but you lose the interactive guidance. The real power is Kiro adapting its responses based on the file.

---

### Q: Do I need to use all 8 core modes?
**A:** No. Use the ones relevant to what you're doing. Some projects might need all 8. Some might only need 2-3. Pick what makes sense.

---

### Q: Is this just hype or does it actually help?
**A:** It actually helps, but only if you use it seriously. These are frameworks for thinking, not shortcuts to avoid thinking. If you engage genuinely, you'll level up faster than traditional learning methods.

---

## Project & Usage Questions

### Q: How long does a project take with Project Mode?
**A:** Depends on project scope:
- Simple project: 1-2 weeks
- Medium project: 1-3 months
- Complex project: 3-6+ months

Project Mode guides you through each phase, but you still have to actually build. The mode provides thinking structure, not speed.

---

### Q: Can I use steering files for small personal projects?
**A:** Sure! But they shine on projects where:
- You want to learn professional patterns
- Code quality matters
- You're building something you'll maintain
- You have time to think through it properly

Quick throwaway scripts probably don't need them.

---

### Q: How do I know if the steering file is working?
**A:** You'll notice:
- More thoughtful questioning from Kiro
- Being pushed to think about trade-offs
- Feeling challenged (in a good way)
- Building things more carefully
- Better outcomes

If Kiro is just giving you code snippets, you're not using the file right.

---

### Q: Can I use multiple steering files on one project?
**A:** Yes! For different phases or topics:
- Design phase: Architecture Mode
- Testing phase: Testing Mode
- Building phase: Project Mode
- When stuck: Debugging Mode
- Before launch: DevOps Mode

Just use them sequentially or in new chats.

---

### Q: What if I disagree with a steering file's approach?
**A:** That's fine. The files encode best practices, but your context is unique. Adapt them. Even disagreement teaches you something.

---

## Customization & Modification

### Q: Can I modify steering files for my team?
**A:** Yes! You can:
1. Make a copy with a different name
2. Adjust the tone, process, or specific guidance
3. Share with your team
4. Make custom versions for specific projects

Example: `steering-architecture-startup.md` (customized for startups)

---

### Q: Can I create my own steering files?
**A:** Yes! Use the template included (`steering-file-template.md`) to create custom files. You can encode your team's specific practices, preferences, or methodologies.

---

### Q: Should I share these with my team?
**A:** Absolutely. These are meant for teams. Your team using the same steering files means:
- Consistent thinking patterns across the team
- Shared language and frameworks
- Better code reviews and decisions
- Faster junior dev development

---

## Learning & Career Questions

### Q: Will these actually help my career?
**A:** Yes, if you use them to build real projects. Because:
- You're learning to think like senior engineers
- Your portfolio will show professional-quality code
- You'll interview better (can articulate thinking)
- You'll contribute better to teams

But only if you actually use them on real work.

---

### Q: I'm already experienced. Are these for me?
**A:** Maybe. If you:
- Want to level up to the next tier
- Lead a team and want consistent thinking
- Work in areas where you're not expert
- Want to formalize your own thinking
- Can use them as templates for your team

Experienced devs often use them to codify their expertise for their team.

---

### Q: How do these compare to courses?
**A:** Different tools:
- **Courses:** Teach you concepts, structured learning
- **Steering files:** Guide your thinking on real projects

Ideal: Do a course, then use steering files to apply what you learned. Much faster than either alone.

---

### Q: Can these replace a senior engineer/mentor?
**A:** No, but they're better than nothing and complement mentorship:
- They give you frameworks to think about
- Your mentor can then guide you more effectively
- They speed up your learning between mentor interactions
- They force you to think before asking

Think of them as: "Expert guidance when your mentor isn't available"

---

## Technical Questions

### Q: How do I load a steering file in Kiro?
**A:** Type `#filename.md` in your chat:
```
#steering-architecture.md
I need help designing a system
```

---

### Q: Can I load multiple files at once?
**A:** Not in one reference. But you can:
1. Load one file
2. Use it for that topic
3. Load another file for a different topic
4. Or start a new chat with a different file

---

### Q: Do I need to reference the file every message?
**A:** No. Load it once at the start of a chat, and it stays active for the entire conversation.

---

### Q: What if I want to stop using a steering file?
**A:** Just start a new chat without referencing it. Or reference a different file to switch frameworks. The file is only active in the chat where you loaded it.

---

### Q: Can I use steering files with other AI tools?
**A:** Not directly (they're built for Kiro). But you could adapt the concepts:
- Copy the content into ChatGPT
- Use it as a system prompt
- Works, but loses some magic that Kiro integration provides

Best to use with Kiro.

---

## Practical Questions

### Q: When should I use Project Mode vs. individual modes?
**A:** 
- **Project Mode:** Building something significant from idea to production
- **Individual Modes:** Specific guidance on one aspect of your current work

Example:
- Project Mode: "I'm building a startup MVP"
- Architecture Mode: "I need to design the system" (part of a bigger project)
- Testing Mode: "I need test strategy for this component" (quick question)

---

### Q: What if my project doesn't fit the 10 phases of Project Mode?
**A:** Most projects follow these phases roughly:
1. Understand what you're building
2. Research & validate
3. Design the system
4. Plan testing
5. Set up environment
6. Implement
7. Review code
8. Test everything
9. Deploy
10. Monitor & improve

If your project skips a phase (e.g., personal script), just skip that phase.

---

### Q: Can I use these for debugging production issues?
**A:** Yes! Load Debugging Mode:
```
#steering-debugging.md
Production database connections are timing out randomly.
Help me debug this systematically.
```

Excellent for systematic troubleshooting.

---

### Q: Should I use these for code interviews?
**A:** Not during the interview itself (can't use Kiro). But:
- Practice with steering files beforehand
- Learn the thinking patterns
- Interview confidently because you've practiced

Interviewers WANT to see the thinking patterns these encode.

---

## Motivation & Mindset Questions

### Q: I don't have time for "thinking frameworks." I just need to code.
**A:** You're right, you don't have time. Frameworks save time by preventing:
- Bugs that take days to debug
- Redesigns that waste weeks
- Production failures at 3 AM
- Technical debt that slows you forever

Spend 2 hours thinking now, save 20 hours later.

---

### Q: What if I use these but still don't level up?
**A:** Most likely reasons:
1. You're reading, not building (need to apply)
2. You're not engaging seriously (going through motions)
3. You're skipping the hard thinking (looking for shortcuts)
4. You're not doing this on real projects (just theory)

Steering files + real project building + genuine engagement = leveling up

---

### Q: Is this only for people who want to be "senior engineers"?
**A:** No. These help anyone who wants to:
- Write better code
- Build things that last
- Understand systems better
- Impress people with quality
- Enjoy coding more

Doesn't require senior engineer ambitions. Just caring about quality.

---

### Q: How do I stay motivated to use these?
**A:** 
1. Use them on real projects you care about
2. Notice the improvements (better code, fewer bugs)
3. See the career progression
4. Share your learnings with others
5. Remember: You're building skills that will serve you forever

---

## Troubleshooting Questions

### Q: I loaded a steering file but Kiro isn't responding differently.
**A:** 
1. Verify the filename is correct (exact case and extension)
2. Ask real questions with context, not generic ones
3. Engage seriously (they respond to engagement)
4. Wait a moment for Kiro to acknowledge the file

---

### Q: I want to use a steering file but it doesn't fit my exact situation.
**A:** That's okay. They're frameworks, not rigid recipes. Adapt them:
- Skip phases that don't apply
- Modify questions to fit your context
- Use the thinking pattern, adjust the specifics

The frameworks are flexible.

---

### Q: Can I use steering files offline?
**A:** Not in a meaningful way (Kiro is online). You can read them offline, but the real power is Kiro's adaptive responses based on the file.

---

## Finally...

### Q: Is there anything I'm missing?
**A:** You might be overthinking it. The best way to understand is to:
1. Pick one steering file
2. Load it in Kiro
3. Work on a real problem
4. Experience it

The value becomes obvious through use, not explanation.

---

### Q: Where do I go from here?
**A:** 

1. **Ready to start?** → Pick a real problem, load the appropriate steering file, begin
2. **Want more details?** → Read `how-to-use-steering-files.md`
3. **Want to understand the philosophy?** → Read `what-are-steering-files.md`
4. **Want to see an example?** → Look at `project-full-lifecycle-example.md`
5. **Want to create your own?** → Use `steering-file-template.md`
6. **Getting started with Kiro?** → Read `kiro-integration-guide.md`

Pick what you need, skip the rest.

---

## One More Thing

The best question isn't answered here: **"Will I actually use these?"**

That's on you. These are tools for people who are serious about leveling up. If that's you, they'll change how you work.

If not, no problem. Everyone has different goals.

But if you ARE serious, these will accelerate your growth in ways that take others years to achieve on their own.

That's not hype. That's just what happens when you code with expert guidance and genuine intention.

---

**Still have questions?** That's what the other docs are for. Or just dive in and learn through doing.

The best learning happens in practice.
