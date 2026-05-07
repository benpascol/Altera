# Prompt Engineering Patterns

> **Prompt Engineering isn't about better prompts. It's about architectural thinking applied to AI behavior.**

Steering files library for building professional-grade software with AI coding assistants. Encode senior developer thinking into your development workflow.

![Agentic Smart Engineering - Ready to Use](https://img.shields.io/badge/agentic-smart%20engineering-blue?style=for-the-badge)
![Open Source](https://img.shields.io/badge/open%20source-%E2%9C%A8-brightgreen?style=for-the-badge)

---

## 🎯 What Is This?

This repository contains **steering files** - specialized markdown documents that define exactly how AI coding assistants (like Kiro, Claude, or any AI tool) should think and respond in different development scenarios.

Instead of asking "write me good code," you load a steering file and the AI operates through that expert framework for the entire conversation.

**Think of it like:**
- Hiring a senior engineer and briefing them before work starts
- An architect defining specifications before construction begins
- A conductor's sheet music telling every musician how to play

**The result:** Code that's production-ready, well-tested, and architecturally sound—before you even write most of it.

---

## 🚀 Quick Start

### For Kiro IDE Users

1. **Copy a steering file** from the `steering-files/core/` directory
2. **In Kiro, reference it:**
   ```
   #steering-architecture.md
   I'm designing a notification system. Help me think through the architecture.
   ```
3. **Kiro loads the file and adapts** its responses for the entire chat
4. **Work through the framework** with expert guidance

### For Any AI Tool

1. **Pick a steering file** that matches your need
2. **Copy the content** into your AI chat or system prompt
3. **Use it as a framework** for your conversation

---

## 📁 What's Inside

### **Core Steering Files (8)**
Professional thinking frameworks for different engineering domains:

- `steering-architecture.md` - System design & architecture decisions
- `steering-code-review.md` - Code quality & production readiness review
- `steering-debugging.md` - Systematic troubleshooting & root cause analysis
- `steering-research.md` - Rigorous technical investigation & learning
- `steering-testing.md` - Strategic test planning & test design
- `steering-performance.md` - Data-driven performance optimization
- `steering-devops.md` - Reliable deployment & operations
- `steering-refactoring.md` - Technical debt management & safe refactoring

### **Project Mode (1)**
Complete guide for building projects from idea to production:

- `steering-project-full-lifecycle.md` - All 10 phases: clarify → research → design → test → build → review → validate → deploy → monitor → improve

### **Documentation (5)**
Learn how to use and create steering files:

- `docs-getting-started.md` - Quick orientation & what this is
- `docs-what-are-steering-files.md` - Philosophy & concepts
- `docs-how-to-use-steering-files.md` - Practical usage patterns
- `docs-kiro-integration-guide.md` - Kiro IDE specific setup
- `docs-faq.md` - Common questions answered

### **Examples (1)**
See it in action:

- `examples-project-full-lifecycle.md` - Real walkthrough: task management tool from idea to production

### **Templates (1)**
Create your own:

- `templates-steering-file-template.md` - Guide for building custom steering files

---

## 📊 Directory Structure

```
prompt-engineering-patterns/
│
├── steering-files/
│   ├── core/
│   │   ├── steering-architecture.md
│   │   ├── steering-code-review.md
│   │   ├── steering-debugging.md
│   │   ├── steering-research.md
│   │   ├── steering-testing.md
│   │   ├── steering-performance.md
│   │   ├── steering-devops.md
│   │   └── steering-refactoring.md
│   │
│   └── project/
│       └── steering-project-full-lifecycle.md
│
├── docs/
│   ├── docs-getting-started.md
│   ├── docs-what-are-steering-files.md
│   ├── docs-how-to-use-steering-files.md
│   ├── docs-kiro-integration-guide.md
│   └── docs-faq.md
│
├── examples/
│   └── examples-project-full-lifecycle.md
│
├── templates/
│   └── templates-steering-file-template.md
│
└── README.md (you are here)
```

---

## 💡 How Steering Files Work

### Without Steering Files
```
You: "How should I test this component?"
AI: "Here's a simple example with Jest..."
[Generic response, resets each message]
```

### With Steering Files
```
You: #steering-testing.md
    How should I test this component?

AI: [Enters Testing Mode]
    Before we discuss implementation, let's think strategically:
    - What business logic matters most?
    - What failures would hurt users?
    - What's your coverage target?
    [Framework-guided response, consistent throughout chat]
```

**The difference:** Consistent expert thinking throughout your entire project.

---

## 🔥 Real-World Example

**Task:** Build a real-time notification system

**Without steering files:**
- Jump to coding → refactor when design issues appear
- Test haphazardly → bugs in production
- Deploy quickly → problems after launch
- Weeks of firefighting

**With steering files:**

1. **Architecture Mode** → Design properly first
2. **Testing Mode** → Plan tests before code
3. **Project Mode** → Guide through 10 phases systematically
4. **DevOps Mode** → Deploy safely with monitoring
5. **Result:** Production-ready system, fewer bugs, team confidence

See the full example: `examples-project-full-lifecycle.md`

---

## 📚 Documentation Map

| Need | Read This |
|------|-----------|
| "What is this?" | `docs-getting-started.md` |
| "Why should I use this?" | `docs-what-are-steering-files.md` |
| "How do I actually use it?" | `docs-how-to-use-steering-files.md` |
| "I use Kiro, what now?" | `docs-kiro-integration-guide.md` |
| "I have questions" | `docs-faq.md` |
| "Show me a real example" | `examples-project-full-lifecycle.md` |
| "I want to create my own" | `templates-steering-file-template.md` |

---

## 🎯 Core Principles

These steering files are built on proven principles:

### 1. **Think First, Code Second**
Design and plan before implementation. Saves weeks of refactoring.

### 2. **Frameworks Over Recipes**
Learn how to think, not just what to do. Patterns transfer everywhere.

### 3. **Senior Developer Patterns**
Encode the thinking of experienced engineers. Accessible to everyone.

### 4. **Strategic Over Exhaustive**
Focus on what matters. Avoid premature optimization and perfect code.

### 5. **Learning Through Building**
Theory + practice together. Understand by doing.

---

## 🤝 Contributing

I welcome contributions! Ways to help:

- **Suggest improvements** - Have ideas? Create an issue.
- **Add variations** - Create domain-specific steering files

### Contributing Guide
1. Fork the repository
2. Create your branch (`git checkout -b feature/amazing-steering-file`)
3. Add or improve a steering file
4. Submit a pull request with description of changes

---

## 📖 Philosophy

These steering files represent a shift in how developers work with AI:

**From:** "AI, give me a solution"  
**To:** "AI, think like an expert and guide me through this"

This is more work upfront. It's also more learning, better code, and real understanding.

The patterns here? They're the same thinking that separates junior developers from senior ones. These files make that thinking accessible to anyone willing to learn.

---

## 🙏 Credits

**Inspired by** [Oron Avisrur](https://www.linkedin.com/in/oron-avisrur-40a7a28/) and his foundational work on prompt engineering.

**Built for** [Kiro IDE](https://aws.amazon.com/kiro/) - Amazon's spec-driven development IDE that makes this possible.

**Created by** developers who believe that better thinking leads to better code.

---

## 📞 Questions?

- **How do I use these?** → See `docs-how-to-use-steering-files.md`
- **Does this work with [AI tool]?** → See `docs-faq.md`
- **Can I customize these?** → See `templates-steering-file-template.md`
- **Something else?** → Open an issue, we're here to help

---
