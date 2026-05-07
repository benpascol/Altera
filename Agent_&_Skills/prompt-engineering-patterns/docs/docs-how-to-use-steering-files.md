# How to Use Steering Files

## The Basics

### Step 1: Load a Steering File
In any Kiro chat, reference a steering file like this:

```
#steering-architecture.md
I need to design a new system for real-time notifications
```

Or just load it by itself:

```
#steering-project-full-lifecycle.md
```

**That's it.** Kiro now reads the file and adapts for the entire conversation.

---

### Step 2: Engage With The Framework
The steering file loaded, now ask your questions within that framework.

**Example: With Architecture Mode**
```
#steering-architecture.md

I'm building a real-time notification system. Users create events, 
other users get notified instantly. Where do I start?

// Kiro responds as an architect would:
// - Asks clarifying questions about requirements
// - Challenges your assumptions
// - Forces you to think about trade-offs
// - Guides you through architectural thinking
```

**Example: With Testing Mode**
```
#steering-testing.md

I have a React component that fetches user data and displays it. 
How should I test this?

// Kiro responds as a test strategist would:
// - Asks what business logic matters
// - Questions what failures would hurt users
// - Discusses coverage targets
// - Helps design test strategy (not just implementation)
```

---

### Step 3: Stay In The Mode
The steering file stays active for the **entire conversation**.

You don't need to reference it again. Kiro continues thinking through that lens.

```
#steering-architecture.md

Q1: I'm building a notification system...
A1: [Architecture thinking]

Q2: What about the database design?
A2: [Still thinking architecturally]

Q3: How do I handle failures?
A3: [Still architectural lens]
```

---

## Common Patterns

### Pattern 1: Single Question, Deep Dive
```
#steering-debugging.md

I've got a bug where my API sometimes returns stale data.
Help me debug this systematically.

// You get a framework for systematic debugging
// Kiro guides you through hypothesis formation
// You discover root cause together
```

**Best for:** One specific problem you want to think through deeply

---

### Pattern 2: Multi-Session Project
```
#steering-project-full-lifecycle.md

I'm starting a new product. Let's go through this together.

// Phase 0: Clarify the idea
// ...answers...

// Phase 1: Research & validation
// ...answers...

// Continue through all 10 phases
```

**Best for:** Building something significant and wanting guidance throughout

---

### Pattern 3: Review & Feedback
```
#steering-code-review.md

Here's my code for a user authentication system.
Please review it carefully.

// Kiro reviews through the code review lens
// Checks correctness, security, design, performance
// Gives specific, actionable feedback
```

**Best for:** Getting professional code review feedback

---

### Pattern 4: Learning & Research
```
#steering-research.md

I need to pick between PostgreSQL and MongoDB for this project.
How should I research this decision?

// Kiro structures your research
// Pushes you toward primary sources
// Helps you evaluate trade-offs
// Guides you to a justified decision
```

**Best for:** Making important technical decisions carefully

---

### Pattern 5: Performance Investigation
```
#steering-performance.md

My app is slow. Users are complaining. Where do I start?

// Kiro pushes you to profile first
// Helps identify actual bottlenecks
// Guides strategic optimization
// Keeps you from premature optimization
```

**Best for:** Performance problems that need systematic investigation

---

## When to Switch Files

**You can load a new steering file** to change your thinking mode mid-project:

```
Chat Session 1:
#steering-architecture.md
[Design your system thoroughly]

Chat Session 2:
#steering-testing.md
[Plan your test strategy]

Chat Session 3:
#steering-project-full-lifecycle.md
[Build through all phases]
```

Or even within the same chat:

```
#steering-architecture.md
[Design phase complete]

NOW: #steering-code-review.md
[Review some code you wrote]
```

---

## Pro Tips

### Tip 1: Load Once Per Chat
Load the steering file once at the beginning of a chat. You don't need to reference it again.

**Not ideal:**
```
#steering-testing.md How do I test this?
#steering-testing.md And what about error cases?
#steering-testing.md Any other considerations?
```

**Better:**
```
#steering-testing.md
How do I test this component?

[Answer received]

And what about error cases?

[Answer received - still in testing mode]

Any other considerations?

[Answer received - still in testing mode]
```

### Tip 2: Use Full Filenames
Be precise with filenames:
- ✅ `#steering-architecture.md`
- ✅ `#steering-testing.md`
- ❌ `#testing.md` (might not work)
- ❌ `#test` (won't work)

### Tip 3: Combine With Your Context
Steering files work best with actual context about your project:

**Weak:**
```
#steering-architecture.md
How do I design a system?
```

**Strong:**
```
#steering-architecture.md
I'm building a social media app for photographers to share and sell prints.
Target: 100k users in year 1, run on limited budget. How should I design this?
```

### Tip 4: Ask Real Questions
Steering files guide your thinking. Engage seriously:

**Weak:**
```
#steering-project-full-lifecycle.md
Start phase 0
```

**Strong:**
```
#steering-project-full-lifecycle.md
I want to build a tool that helps junior devs learn coding.
Let's start with phase 0. I've got some ideas but want to think through this properly.
What questions should I answer first?
```

### Tip 5: Don't Rush Phases
With project mode, don't speed through. Each phase has value:

**Rushing:**
```
Phase 0: [5 minutes answering questions]
On to Phase 1!
```

**Better:**
```
Phase 0: [Think deeply, answer thoroughly]
Phase 0 output: Solid project brief with real clarity
Phase 1: [Now research with specific focus]
```

---

## How Long Does Each Mode Take?

**Single-Question Modes:** 15-60 minutes  
- Architecture: How long you spend designing
- Code Review: How long you spend reviewing code
- Debugging: Until you find root cause
- Research: Until you have a justified decision

**Project Mode:** Weeks (ongoing)  
- Phase 0: 1-2 hours
- Phase 1: 2-4 hours
- Phase 2: 4-8 hours
- Phases 3-10: Weeks as you actually build

---

## Common Mistakes

### Mistake 1: Loading, Then Not Engaging
```
#steering-architecture.md
Tell me how to design a system
```

**Problem:** You're asking Kiro to do the thinking instead of guiding your thinking.

**Better:**
```
#steering-architecture.md
I'm designing a notification system. What clarifying questions 
should I answer first?
```

---

### Mistake 2: Asking For Quick Answers
Steering files aren't for quick answers. They're for deep thinking.

**Not ideal:**
```
#steering-testing.md
What's a quick way to test this component?
```

**Better:**
```
#steering-testing.md
How should I think about testing this component strategically?
What's most important to test and why?
```

---

### Mistake 3: Switching Too Often
Each mode has value. Switching constantly loses continuity.

**Not ideal:**
```
#steering-architecture.md [ask question]
NOW: #steering-testing.md [ask question]
NOW: #steering-performance.md [ask question]
```

**Better:**
```
#steering-architecture.md [complete design thinking]
[Later chat] #steering-testing.md [plan tests]
[Later chat] #steering-performance.md [optimize]
```

---

### Mistake 4: Not Providing Context
Steering files work best when they understand your situation.

**Low context:**
```
#steering-code-review.md
Review my code
```

**High context:**
```
#steering-code-review.md
This is a user authentication module for a healthcare app.
Security is critical, performance matters, needs to handle 10k+ users.
Here's my code...
```

---

## Workflow Examples

### Example 1: Quick Debugging
```
Chat: debugging-session-1

#steering-debugging.md

Problem: My async code sometimes hangs. Here's what happens...

// Framework-guided debugging to root cause
// Fix identified and verified
// Session complete
```

**Time:** 30 minutes  
**Outcome:** Understood bug, learned debugging approach

---

### Example 2: Project From Idea to Shipped
```
Chat: my-product-1

#steering-project-full-lifecycle.md

I want to build a saas tool for developers. Let's go through the process.

// Phase 0: Clarify
// Phase 1: Research
...
// Phase 10: Continuous improvement

// Takes weeks/months, but continuous guidance
// Builds throughout project
```

**Time:** Weeks  
**Outcome:** Professional product, learned entire process

---

### Example 3: Technical Decision
```
Chat: tech-decision-postgres-vs-mongo

#steering-research.md

I need to choose between PostgreSQL and MongoDB for my project.
Help me research this properly.

// Structured research
// Comparison of approaches
// Trade-offs evaluated
// Justified decision made
```

**Time:** 2-3 hours  
**Outcome:** Confident, justified technology choice

---

## Troubleshooting

**Q: The steering file isn't working. Kiro isn't responding differently.**  
A: Make sure you're referencing the exact filename. And engage seriously—Kiro responds to real questions, not generic ones.

**Q: Can I modify steering files?**  
A: Yes! You can create custom versions tailored to your team/project.

**Q: Can I combine multiple steering files?**  
A: Not in one chat directly, but you can switch between them. Or create a custom file that combines concepts.

**Q: What if I don't like the approach in a steering file?**  
A: That's great feedback! The files encode best practices, but your context matters. Adapt them to your situation.

**Q: Should I use these for every conversation?**  
A: No. Use them when you need expert guidance in that domain. Quick questions don't need steering files.

---

## The Real Power

The real power of steering files isn't in the files themselves. It's in the **thinking discipline** they instill.

After using them for a while, you internalize the frameworks. You start thinking like:
- An architect (even without the file)
- A test strategist (automatically)
- A performance engineer (naturally)

The files accelerate you toward that thinking. Then you carry it with you forever.

---

## Next Steps

1. **Pick one steering file** matching something you're working on
2. **Load it and use it** for a real problem
3. **Notice how your thinking changes**
4. **Engage seriously** with the framework
5. **Build something real** through that lens

That's it. The learning happens through doing.

Let's go.
