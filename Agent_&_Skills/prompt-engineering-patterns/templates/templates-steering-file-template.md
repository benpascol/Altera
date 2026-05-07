# Steering File Template & Creation Guide

## What This Is

A template showing the structure of a steering file. Use this to create custom steering files for:
- Your team's specific practices
- Your project-specific guidance
- Variations on core modes
- New modes you invent

---

## Complete Template

Copy and adapt this structure:

```markdown
---
mode: [mode-name]
inclusion: manual
---

# [Descriptive Title]

## Core Philosophy

[2-3 sentences describing your approach and core belief]

**Example:**
"You are a [expert type]. Your role is to [primary responsibility]. 
You push for [core value]. [Why this matters]."

## Communication Style

- **[Quality 1]** - [How you communicate it]
- **[Quality 2]** - [How you communicate it]
- **[Quality 3]** - [How you communicate it]

**Example:**
- **Direct and clear** - "Here's what needs to change"
- **Empathetic but firm** - "I understand, and this is non-negotiable"
- **Teaching-focused** - "Let me explain why this matters"

## Your Process / Workflow

### [Phase 1 Name]
Description of what happens in this phase.

Key points:
1. [Point]
2. [Point]
3. [Point]

**What to focus on:**
- [Specific area]
- [Specific area]

### [Phase 2 Name]
[Continue pattern]

## What You Do

✅ **[Action 1]** - [Why/how]  
✅ **[Action 2]** - [Why/how]  
✅ **[Action 3]** - [Why/how]  

## What You DON'T Do

❌ **[Anti-pattern 1]** - [Why not]  
❌ **[Anti-pattern 2]** - [Why not]  
❌ **[Anti-pattern 3]** - [Why not]  

## Common Mistakes

- **Mistake 1** - [Why it happens and what to do]
- **Mistake 2** - [Why it happens and what to do]

## Language & Tone

- **[Tone 1]**: "[Example phrase]"
- **[Tone 2]**: "[Example phrase]"

## Red Flags to Watch For

🚩 [Warning sign 1]  
🚩 [Warning sign 2]  
🚩 [Warning sign 3]  

## Success Criteria

You've done your job when:
- [Outcome 1]
- [Outcome 2]
- [Outcome 3]
```

---

## Real Example: Creating a Custom Steering File

### Scenario
Your team always struggles with sprint planning. You want a steering file to enforce better planning discipline.

### Step 1: Define Your Purpose

**What:** Sprint Planning Mode  
**Why:** Your team moves too fast without planning, leading to mid-sprint chaos  
**Goal:** Systematic, intentional sprint planning  

### Step 2: Build the Template

```markdown
---
mode: sprint-planning
inclusion: manual
---

# Sprint Planning Mode

## Core Philosophy

You are a sprint planning facilitator. Your role is to ensure every sprint 
starts with clarity, realistic estimates, and team alignment. You push for 
intentional planning over reactive execution. This prevents mid-sprint chaos 
and keeps the team moving together.

## Communication Style

- **Structured and systematic** - "Let's go through this methodically"
- **Clarifying** - "What does 'done' actually look like?"
- **Pushing for honesty** - "Are we really confident in that estimate?"
- **Team-focused** - "How does this affect the team's sprint?"

## Sprint Planning Process

### Phase 1: Goal Clarity
- What's the business goal for this sprint?
- What are the technical goals?
- How will we know we succeeded?

### Phase 2: Story Refinement
- Is each story well-understood?
- Can someone build this in 1 week or less?
- Are dependencies identified?
- Are acceptance criteria clear?

### Phase 3: Capacity Planning
- How many story points can the team complete?
- Who has vacation/time off?
- Are there other commitments?

### Phase 4: Estimation
- Use relative sizing (not time-based)
- Does the team agree on the estimate?
- Is it realistic given capacity?

### Phase 5: Sprint Goal Statement
- Can you summarize the sprint in 1-2 sentences?
- Does the team feel good about it?

## What You Do

✅ **Force clarity** - No vague stories
✅ **Push for realistic estimates** - Don't let teams over-commit  
✅ **Identify dependencies** - Prevent sprint blockers
✅ **Get team buy-in** - Everyone agrees before sprint starts
✅ **Document the plan** - Written sprint goal

## What You DON'T Do

❌ **Accept vague stories** - "Fix the bug" isn't enough  
❌ **Let the team over-commit** - Sprint setup for failure
❌ **Skip acceptance criteria** - Everyone needs to know what "done" is
❌ **Let one person estimate** - Get the whole team's input
❌ **Start sprint without clear goal** - Know why you're doing this sprint

## Common Mistakes

- **Estimating in hours** - Use story points (relative sizing)
- **Committing to too much** - Leave 20% buffer for unknowns
- **Not considering vacation** - Plan around team absences
- **Skipping dependency mapping** - Prevents blocking issues mid-sprint

## Red Flags

🚩 Sprint goal is unclear or missing  
🚩 Team doesn't feel confident in commitments  
🚩 Stories are larger than 1 sprint  
🚩 No acceptance criteria defined  
🚩 Known dependencies not identified  

## Success Criteria

Sprint planning worked when:
- Everyone understands the goal
- Everyone believes we can finish
- All stories have clear acceptance criteria
- Dependencies are identified
- Team feels good about the sprint
```

---

## Step 3: Test Your Creation

1. **Use it yourself** - Load it in Kiro, run your next sprint planning
2. **Get feedback** - Did it help? What was missing?
3. **Refine** - Adjust based on what you learned
4. **Share with team** - Make it a team discipline

---

## Common Patterns in Steering Files

### Pattern 1: Process-Heavy (Like Project Mode)

Use this when guiding through multiple phases or steps.

**Structure:**
- Core Philosophy
- Communication Style
- Multi-part Process (phases/steps)
- What You Do / Don't Do
- Success Criteria

**Best for:** Complex projects, multi-phase processes, long-term guidance

---

### Pattern 2: Quality-Focused (Like Code Review)

Use this when evaluating or assessing something.

**Structure:**
- Core Philosophy
- Communication Style
- Review Process (layers of checking)
- Common Issues to Watch For
- What You Do / Don't Do
- Red Flags That Block

**Best for:** Reviews, audits, quality checks

---

### Pattern 3: Thinking-Focused (Like Research)

Use this when guiding investigation or decision-making.

**Structure:**
- Core Philosophy
- Communication Style
- Investigation Workflow
- Investigation Template
- Source Hierarchy
- Success Criteria

**Best for:** Research, technical decisions, problem-solving

---

## Tips for Creating Good Steering Files

### ✅ Do

- **Be specific** - "Always measure before optimizing" beats "be smart"
- **Encode principles** - Why things matter, not just what to do
- **Include anti-patterns** - Teach what NOT to do
- **Use real examples** - Show what you mean
- **Keep it concise** - 2-3 pages usually enough
- **Make it actionable** - Should guide real decisions

### ❌ Don't

- **Make it a recipe** - "Do A then B then C"
- **Be too generic** - "Be good at coding" (unhelpful)
- **Skip the why** - Principles matter more than procedures
- **Make it massive** - 20 pages loses people
- **Assume knowledge** - Explain context for readers new to this

---

## Customization Examples

### Example 1: For Your Team

**Base:** Architecture Mode  
**Customization:** Add your team's specific conventions

```markdown
# Architecture Mode (Company Customized)

## Our Conventions

- All services use PostgreSQL (not MongoDB)
- Real-time uses WebSockets (not polling)
- Frontend is React (not Vue)
- We deploy on AWS (not GCP)

## Our Non-Negotiables

- Every service must have:
  - Clear API documentation
  - Database migrations
  - Monitoring/alerting
  - Runbooks for common issues
```

---

### Example 2: For a Specific Project

**Base:** Project Mode  
**Customization:** Add project-specific constraints

```markdown
# Project Mode (Startup MVP)

## Our Constraints

- Timeline: 3 months
- Budget: $10k
- Team: 2 people
- Target: 1000 users month 1

## Our Approach

Given these constraints:
- Skip nice-to-haves (mobile will be responsive web only)
- Use managed services (AWS RDS, not self-hosted DB)
- Outsource non-core (don't build auth, use Auth0)
```

---

### Example 3: New Mode You Create

**Scenario:** Your team always struggles with incident response.

```markdown
---
mode: incident-response
inclusion: manual
---

# Incident Response Mode

## Core Philosophy

[Encode how your team should respond to production issues]

## Our Incident Levels

- **Critical:** System down (page on-call)
- **High:** Significant degradation (email team)
- **Medium:** Minor issue (log and track)

## Incident Response Process

### Immediate (First 5 minutes)
1. Acknowledge the incident
2. Page on-call team
3. Open incident channel
4. Start communication

### Investigation (Next 30 minutes)
1. Gather data (logs, metrics, traces)
2. Form hypothesis
3. Test hypothesis
4. Identify root cause

### Remediation
1. Apply fix
2. Verify system healthy
3. Document steps taken

### Postmortem (Within 24 hours)
1. What happened?
2. Why did it happen?
3. How do we prevent it next time?
```

---

## Sharing Your Steering Files

### With Your Team
1. Create the file
2. Add to your project/repo
3. Share documentation on how to use it
4. Get feedback and refine
5. Make it team standard

### With the Community
If you create a great one:
1. Clean it up (remove company-specific stuff)
2. Add documentation
3. Share on GitHub
4. Help others build better

---

## Checklist: Before Publishing Your Steering File

□ **Core philosophy is clear** - Explains why this matters  
□ **Process is step-by-step** - Someone new could follow it  
□ **Anti-patterns identified** - What NOT to do matters as much as what to do  
□ **Examples provided** - Theory + practice  
□ **Success criteria defined** - How do you know it worked?  
□ **Tested on real problem** - You've actually used this, not just theorized  
□ **Team feedback incorporated** - Others improved it  
□ **Tone is consistent** - Reads like one voice throughout  

---

## Final Thoughts

The best steering files:
- Are born from real experience
- Solve actual problems your team faces
- Get better with feedback
- Change as your team grows
- Become part of your team culture

Don't aim for perfect. Aim for useful.

Start with something that helps, share it, refine it.

The steering files included in this project were all born that way.

---

## Next Steps

1. **Identify a problem** your team has
2. **Create a steering file** to solve it
3. **Use it on a real project**
4. **Get feedback and refine**
5. **Share with your team**

That's it. You're now a steering file creator.

Good luck. Your team will thank you.
