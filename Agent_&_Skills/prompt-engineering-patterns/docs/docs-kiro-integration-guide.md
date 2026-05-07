# Kiro Integration Guide

## How Steering Files Work With Kiro

Steering files are designed specifically for **Kiro IDE**. Here's everything you need to know to use them.

---

## Quick Setup

### 1. Access AGENT STEERING
In Kiro IDE, look at the left sidebar. You'll see:
```
▼ AGENT STEERING
  🔗 my-preferences
```

This is where steering files go.

### 2. Add Steering Files
You can add steering files in two ways:

**Method A: Reference by Filename**
In any chat, just type:
```
#steering-architecture.md
```

Kiro recognizes this and loads the file for the conversation.

**Method B: Add to Your Collection**
(Contact Kiro documentation for setup details on making files persistently available)

### 3. Verify It Loaded
After referencing a steering file, you'll see it noted. Kiro will respond with awareness of the framework.

**Example:**
```
User: #steering-testing.md
       How should I test my API endpoint?

Kiro: I see you've loaded testing mode. Before we discuss implementation,
      let's think strategically about testing...
      [Framework-guided response]
```

---

## Using Steering Files in Kiro

### Single Chat, One Mode

```
[Start new chat]

User: #steering-architecture.md
      I'm designing a notification system. Users push events,
      others get notified in real-time. Help me think through the design.

Kiro: [Responds as architect through entire framework]

User: What about scalability to 1M users?

Kiro: [Still thinking architecturally]

User: Database choices - PostgreSQL or Redis?

Kiro: [Still in architecture framework]
```

**Key:** The file stays active throughout the conversation.

---

### Switching Files Mid-Chat

You can load a different steering file to change your thinking mode:

```
[Existing chat with architecture mode]

User: NOW: #steering-code-review.md
      Let me show you some code I wrote based on that design

Kiro: [Switches to code review mode]
      [Reviews code through that framework]
```

---

### Multiple Chats, Different Modes

For larger projects, use different chats for different phases:

```
Chat 1 - Design Phase:
#steering-architecture.md
[Design the system]

Chat 2 - Testing Strategy:
#steering-testing.md
[Plan tests]

Chat 3 - Full Project Guidance:
#steering-project-full-lifecycle.md
[Build through all phases]
```

---

## Frontmatter Explained

Each steering file starts with:
```yaml
---
mode: architecture
inclusion: manual
---
```

### What This Means

**`mode: architecture`**  
- Identifies what type of steering file this is
- Used for organization and reference

**`inclusion: manual`**  
- "Manual" means you reference it explicitly with `#filename`
- You only need to reference it ONCE per chat
- Then it stays active for the entire chat

### How It Works

1. You type `#steering-architecture.md`
2. Kiro reads the file (because `inclusion: manual`)
3. The file content becomes context for that chat
4. Everything you discuss happens through that lens
5. When you close the chat, the file is no longer active

---

## File Naming Convention

All steering files follow this pattern:
```
steering-[mode-name].md
```

Examples:
- `steering-architecture.md`
- `steering-testing.md`
- `steering-code-review.md`
- `steering-project-full-lifecycle.md`

When referencing in Kiro:
```
#steering-architecture.md  ✅ Correct
#architecture.md           ❌ Might not work
#steering-arch             ❌ Won't work
```

**Always use the full filename.**

---

## Common Kiro Interactions

### Interaction 1: Loading and Asking

```
User: #steering-research.md
      I need to decide between Next.js and Nuxt for my project.
      How should I research this decision?

Kiro: I see you're in research mode. Let's structure this properly:
      
      First, help me understand your context:
      1. What's your team's experience level?
      2. What's your timeline?
      3. What's most important: speed, SEO, maintainability?
      
      Once I understand these constraints, we can:
      - Identify official sources to research
      - Compare the approaches systematically
      - Evaluate trade-offs for YOUR situation
      - Make a justified decision
```

---

### Interaction 2: Following the Framework

```
User: Great. We're a team of 2 junior devs. We need something fast.
      Maintainability matters, SEO matters, we have 3 weeks.

Kiro: Perfect context. Let's research this strategically:
      
      Next.js vs Nuxt comparison:
      [Framework-guided analysis based on YOUR constraints]
      
      Here's what I'd research first:
      1. Official documentation...
      2. Real-world case studies...
      3. Performance benchmarks...
      
      And here's where I'd NOT spend time:
      - Obscure edge cases
      - Features you won't use
      - Hype-driven comparisons
```

---

### Interaction 3: Continuous Guidance

```
User: Good point. I didn't consider SEO implications. Let me research.
      [later in conversation]
      I've read through the docs. Here's what I found:
      [Summary of findings]

Kiro: Great research. Let me help you interpret this:
      [Still in research framework, guiding analysis]
      
      Based on your constraints, here's my recommendation:
      [Justified decision based on the framework]
```

---

## Tips for Using Steering Files in Kiro

### Tip 1: Be Specific About Context

**Weak:**
```
#steering-architecture.md
Design a system for me
```

**Strong:**
```
#steering-architecture.md
I'm building a project management tool for remote teams.
Expected: 10k users year 1, must support real-time collaboration,
team of 3 junior developers, AWS budget of $500/month.
Help me design this.
```

Kiro responds better with actual context.

---

### Tip 2: Reference Once, Use Throughout

**Not necessary:**
```
#steering-testing.md How do I test this function?
#steering-testing.md And what about error cases?
#steering-testing.md Should I use mocks?
```

**Better:**
```
#steering-testing.md
How do I test this function?

And what about error cases?

Should I use mocks?

// All in the same chat, file loaded once
```

---

### Tip 3: Use For Real Problems

Steering files work best on actual projects you're building:

**Research Mode:**
```
#steering-research.md
I actually need to choose between PostgreSQL and MongoDB
for my startup. Help me research this properly.
```

**Project Mode:**
```
#steering-project-full-lifecycle.md
I'm starting a new SaaS product. Let's build this right,
from idea through production.
```

---

### Tip 4: Engage Seriously

Steering files require participation:

**Passive approach:**
```
#steering-debugging.md
My code has a bug. Fix it.
```

**Active approach:**
```
#steering-debugging.md
I've got a bug where the API returns stale data sometimes.
Here's what I've observed... Help me think through this systematically.
```

Kiro guides your thinking; you do the thinking.

---

## AGENT STEERING vs. AGENT HOOKS

**Quick clarification:**

**AGENT STEERING** (what we're using)
- Steering files like `#steering-architecture.md`
- For chat-based guidance
- Changes how Kiro thinks and responds
- Load with `#filename` in chats

**AGENT HOOKS** (future feature)
- Automatable workflows
- Trigger on specific events
- Different purpose (automation, not guidance)

For now, focus on **AGENT STEERING**.

---

## Troubleshooting

### Issue: File Not Loading
**Problem:** You typed `#steering-testing` but nothing happened

**Solution:** Use the full filename: `#steering-testing.md`

---

### Issue: Kiro Not Responding Differently
**Problem:** Loaded file but Kiro isn't using the framework

**Solution:** 
1. Make sure file loaded (check it's referenced correctly)
2. Ask real questions, not generic ones
3. Provide actual context about your project
4. Engage seriously with the framework

---

### Issue: Want to Switch Frameworks
**Problem:** Loaded architecture mode but now want testing mode

**Solution:** You can load a new file:
```
NOW: #steering-testing.md
This is a different question about testing strategy.
```

Or start a new chat with the new framework.

---

### Issue: File Changes Not Reflected
**Problem:** You updated a steering file but Kiro still uses old version

**Solution:** 
- If `inclusion: manual`: Close the chat and open a new one, reference file again
- The file is only loaded once when you reference it

---

## Workflow Examples

### Example: Debugging a Performance Issue
```
Chat Session: "Performance Problem"

#steering-performance.md

User: My React app is slow. Users complaining. I think it's the database.

Kiro: [Enters performance mode]
      Before we guess, let's measure first.
      - Have you profiled the app?
      - Where is time actually being spent?
      - What's slow: frontend, backend, network?

User: [Profiles the app, shares findings]

Kiro: [Guides toward the actual bottleneck]
      So the network is the issue, not the database.
      Here's how to optimize strategically...
```

---

### Example: Building Full Project
```
Chat Session: "My New Product"

#steering-project-full-lifecycle.md

User: I'm building a coaching platform for developers.
      Let's go through this systematically.

Kiro: Perfect! Let's start with Phase 0.
      [Guides through requirement clarification]

User: [Answers questions, clarifies requirements]

Kiro: Great! Phase 0 is solid.
      Now Phase 1: Research & Validation
      [Next phase guidance]
      
[Chat continues throughout project, phases completed systematically]
```

---

## Best Practices

### ✅ Do

- Load steering files for real, significant problems
- Provide context about your project
- Engage seriously with the frameworks
- Reference the full filename with `.md` extension
- Stay in one framework for one session/topic
- Use appropriate frameworks for your situation

### ❌ Don't

- Load files and expect magic without engagement
- Reference file multiple times (load once per chat)
- Use generic filenames without extensions
- Mix too many files in one conversation
- Expect quick answers (these are for deep thinking)
- Use for trivial questions

---

## Next Steps

1. **Pick a real problem** you're working on
2. **Choose the appropriate steering file** for that problem
3. **Open Kiro IDE** and start a new chat
4. **Type** `#steering-[mode].md` and your question
5. **Engage seriously** with the framework
6. **Think through** the problem with guidance

The documentation is done. Time to put it to work.

Reference this guide when you need to remind yourself of the technical details. But mostly, just use the files. They work better when you're actually building something.

Let's go.
