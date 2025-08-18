# Contributing to the Project

__First, thank you for contributing to my project!__

This plugin was created as a hobby, so I might not always be able to release updates quickly or develop new features.

---

## 1. How can I contribute?

You can help by:
- Fixing bugs
- Implementing checks
- Optimizing existing code

**I recommend checking the Issues or the Todo page** for bugs, features, or checks that need attention.  
The Todo page contains every feature I currently plan to implement, fix, or optimize. I will update it over time, but I will prioritize the listed items first.

---

## 2. What skills do I need?

You don’t need to have all of these skills to contribute, but they can help you work faster. These are **advice**, not rules.

### Recommended skills:
- **PacketEvents** (our packet listener)
- **Spigot/Bukkit API knowledge** (the plugin is built with Spigot)
- **Java** (the plugin is written in Java)
- **Git knowledge** (for pulling, pushing, and committing)

---

## 3. Can I use AI to contribute?

Short answer: **No**.

You can use AI for research or to help understand bugs, but **please do not submit AI-generated code**. It can cause more refactoring, introduce new bugs, and is generally not reliable for this project.

---

## 4. What naming conventions should I use?
For simplicity and also for organization follow this and can eliminate future refactoring.
But here is the list:

- For **variables** use **camelCase** (int speedMultiplier = ...)
- For methods/ functions use **camelCase** (public int getSpeedMultiplier(){...})
- For **classes / enums / interfaces / abstracts** you should use **UpperCamelCase** (enum BreakStateEnum {...})
- For **constant / final variables** please use **UPPER_CASE_WITH_UNDERSCORES** (private final CONSTANT = ...)

That I described is basically the **General Java Naming Convention**.
And this rule is in place because to be organized. If you do not follow these, your push requests will be accepted, but it will need more refactoring that someone will do.




# Set up the repo locally

## First, clone the repo like this:
```
git clone https://github.com/Nerd10000/Aegis.git
```

This will create a local version of the project.

Make sure you have **OpenJDK 23** installed.
Why? I'm using it and I have no problem with it.


