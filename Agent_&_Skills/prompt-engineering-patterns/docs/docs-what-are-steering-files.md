# What Are Steering Files?

## The Concept

A **steering file** is a specialized markdown document that teaches an AI (in this case, Kiro) to think and respond a certain way for the entire conversation.

It's like hiring an expert consultant and briefing them on your project before the engagement. Once you brief them (load the file), they operate through that lens for everything you discuss.

---

## How It Works

### Traditional AI Usage
```
You: Ask a question
AI: Answers based on general training
You: Follow up
AI: Answers again (doesn't remember the context of how to think)
You: Ask another question
AI: Answers generically again
```

**Problem:** Each response is independent. No consistent thinking framework.

### With Steering Files
```
You: Load architecture-mode.md
You: Ask about system design
AI: Responds as an architect would
You: Follow up
AI: Still thinking like an architect (because the file guides behavior)
You: Ask another question
AI: Still responding architecturally
```

**Benefit:** Consistent, expert-level thinking across the entire conversation.

---

## What's In A Steering File

Each steering file contains:

1. **Core Philosophy** - The mindset being transmitted
2. **Communication Style** - How to engage (direct? teaching? challenging?)
3. **Workflow/Process** - Step-by-step methodology
4. **What to Do / What NOT to Do** - Clear boundaries
5. **Success Criteria** - How you know it worked

Example: The **Code Review** steering file includes:
- Philosophy: "Code should be production-ready"
- Style: "Specific, actionable feedback with teaching"
- Process: "Check scope, correctness, design, performance, security, testing, maintainability"
- Do's: "Question assumptions, catch bugs before production"
- Don'ts: "Nitpick formatting, approve code you're unsure about"

When loaded, Kiro acts through this entire framework.

---

## Key Differences From Regular Prompts

| Aspect | Regular Prompt | Steering File |
|--------|---|---|
| **Length** | Usually short | Detailed and comprehensive |
| **Scope** | Single task | Full conversation mode |
| **Thinking** | One-off response | Consistent framework |
| **Learning** | What to do | Why to do it + how to think |
| **Adaptation** | Static | AI adapts to context within framework |
| **Reusability** | Single use | Used across many projects |

---

## How They're Designed

Each steering file encodes the thinking of **senior developers or specialists** in that domain.

**Architecture Mode captures:**
- How architects think about systems
- What questions they ask first
- What trade-offs they consider
- How they evaluate designs
- Red flags they watch for

**Testing Mode captures:**
- How test strategists think
- Focus on ROI, not coverage percentage
- What's worth testing vs. not
- Common testing mistakes to avoid
- How to build testing pyramids

When you load these files, you're essentially getting the thinking process of someone with 10+ years of experience in that area.

---

## The Power of Consistency

Imagine working with a consultant who:
- First session: Thinks one way
- Second session: Thinks differently
- Third session: Inconsistent approach
- Never builds on previous thinking

vs.

A consultant who:
- Understands the framework from day 1
- Applies it consistently
- Challenges you within that framework
- Pushes you toward mastery

**Steering files create the second scenario.**

---

## They're Not Recipes

Important distinction:

**Recipe:** "Do A, then B, then C, and you'll get the result"

**Steering File:** "Here's how to think about this. Ask yourself X, consider Y, evaluate Z. The path depends on your situation."

They're frameworks for thinking, not step-by-step procedures.

**This is better because:**
- Every project is different
- Context matters
- Generic recipes often fail in reality
- Thinking frameworks adapt to any situation

---

## Why They Work

### 1. Consistent Perspective
You're not getting generic advice. You're getting advice through a specific, expert lens.

### 2. Framework Over Answers
You're learning HOW to think, not WHAT to do. This is how you level up.

### 3. Challenge Built In
The files include red flags, anti-patterns, and questions. You're challenged to think more rigorously.

### 4. Reusable Everywhere
You learn principles that apply beyond just this project. To every project.

### 5. Scalable Learning
Instead of reading books or watching videos, you learn by doing, guided by expertise.

---

## What They're NOT

**Not recipes or templates** - They're frameworks for thinking  
**Not ChatGPT jailbreaks** - They're legitimate pedagogical tools  
**Not a replacement for learning** - They accelerate learning by guiding thinking  
**Not one-size-fits-all** - You pick the right one for your situation  
**Not magic** - They require engagement and effort on your part  

---

## The Philosophy Behind Them

The best way to learn is:

1. **Understand the why** - Not just the what
2. **Think through problems** - Not memorize solutions
3. **Get expert guidance** - Not generic advice
4. **Build frameworks** - Not accumulate recipes
5. **Learn through doing** - Not reading

Steering files enable all five.

---

## Comparison: Different Ways to Learn

### Reading a Blog Post
- ✅ Passive, easy
- ❌ One-size-fits-all, no interaction

### Taking a Course
- ✅ Structured, comprehensive
- ❌ Time-intensive, not practice-focused

### Working With a Mentor
- ✅ Personalized, expert guidance
- ❌ Expensive, not always available

### Using Steering Files
- ✅ Expert guidance, personalized to your project, free, always available
- ❌ Requires engagement and thinking

---

## The Mindset Shift

Using steering files requires a different mindset:

**Before:** "Give me the answer"  
**After:** "Help me think through this right"

**Before:** "How do I do X?"  
**After:** "How should I think about X?"

**Before:** "Quick solution please"  
**After:** "Deep understanding, I'm building skills"

This shift is uncomfortable at first. It's also where the real learning happens.

---

## Real-World Example

**Without steering:**
```
You: How should I handle errors in my API?
AI: Here are common error codes: 400, 401, 403, 500...
```

**With DevOps steering:**
```
You: How should I handle errors in my API?
AI: Before we discuss codes, let's think operationally:
   - What failures will users experience?
   - What failures will your team need to respond to?
   - How will you detect failures?
   - What monitoring/alerting do you need?
   - Then we decide: which errors matter and how to respond
```

See? Same question, completely different quality of thinking.

---

## When They're Useful

✅ Starting a new project  
✅ Need expert guidance in a specific area  
✅ Want to level up your thinking  
✅ Building production systems  
✅ Learning from first principles  
✅ Want consistent thinking across a project  

❌ Just need a quick code snippet  
❌ Don't have time to think deeply  
❌ Want shortcuts (these don't provide them)  
❌ Learning a syntax (use documentation)  

---

## The Commitment

Using steering files well requires:

1. **Engagement** - Answer the questions, don't half-ass it
2. **Thinking** - Be ready for your assumptions to be challenged
3. **Discipline** - Follow the frameworks, even when it feels slow
4. **Humility** - Expert guidance means listening even when you disagree
5. **Action** - Build real projects, don't just theorize

**Payoff:** You develop the thinking patterns that separate junior devs from senior ones.

---

## The Long Game

One project built with steering files teaches you more than:
- 10 courses
- 100 blog posts
- 1000 Stack Overflow answers

Because you're not just learning what to do. You're learning how to think like someone who gets it right.

That thinking transfers everywhere.

---

## Next Steps

Now that you understand what steering files are:

1. **Read** `how-to-use-steering-files.md` for practical usage
2. **Check** `kiro-integration-guide.md` for Kiro-specific setup
3. **Look at** `project-full-lifecycle-example.md` to see one in action
4. **Start with** a steering file on something you're actually working on

The theory is done. Let's put them to work.
