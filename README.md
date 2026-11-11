# RabbitMQ Exchange Types — Learning Notes

## 1. Direct Exchange

### Concept
Direct Exchange routes messages to queues by exact matching between the message's Routing Key and the Queue's Binding Key.

### How it works
- Producer sends a message with a Routing Key.
- Exchange compares the Routing Key with Queue Binding Keys.
- Only queues with an exact match receive the message.

### Example

Exchange: `insurance.direct`

| Queue | Binding Key |
|---|---|
| policy.queue | policy.created |
| payment.queue | payment.success |

Routing Key:
```text
policy.created
```

Result: `policy.queue` receives the message.

### Key Point
Direct Exchange uses exact Routing Key matching.

---

## 2. Fanout Exchange

### Concept
Fanout Exchange broadcasts messages to all queues bound to it.

### How it works
- Producer sends a message to the Exchange.
- Exchange forwards the message to every bound queue.
- Routing Keys are ignored.

### Example

Exchange: `insurance.fanout`

| Queue |
|---|
| policy.queue |
| audit.queue |
| notification.queue |

One message is sent to the Exchange.

Result: All three queues receive the message.

### Key Point
Fanout Exchange broadcasts messages to all bound queues.

---

## 3. Topic Exchange

### Concept
Topic Exchange routes messages using pattern matching between Routing Keys and Binding Keys.

### Wildcards

| Wildcard | Meaning |
|---|---|
| `*` | Matches exactly one word |
| `#` | Matches zero or more words |

### Example

Exchange: `insurance.topic`

| Queue | Binding Key |
|---|---|
| policy.queue | insurance.policy.* |
| audit.queue | insurance.# |
| payment.queue | *.payment.* |

### Test Routing Keys

**1.** `insurance.policy.created`

- policy.queue → Match
- audit.queue → Match
- payment.queue → No Match

**2.** `insurance.payment.success`

- policy.queue → No Match
- audit.queue → Match
- payment.queue → Match

### Key Point
Topic Exchange uses Routing Key pattern matching.

---

## 4. Headers Exchange

### Concept
Headers Exchange routes messages based on Message Headers instead of Routing Keys.

### Message Headers
Headers are key-value pairs attached to a message.

Example:
```text
type = policy
priority = high
region = yangon
```

### Binding Arguments

Queue bindings can define matching rules using `x-match`.

| Rule | Meaning |
|---|---|
| `x-match = all` | All specified headers must match |
| `x-match = any` | At least one specified header must match |

`x-match` is a Binding Argument, not a Message Header.

### Example

Exchange: `insurance.headers`

**policy.queue**
```text
x-match = all
type = policy
priority = high
```

**audit.queue**
```text
x-match = any
region = yangon
priority = high
```

### Test Message

```text
type = policy
priority = high
region = yangon
```

Result:
- policy.queue → Match
- audit.queue → Match

### Second Test Message

```text
type = policy
priority = low
region = yangon
```

Result:
- policy.queue → No Match
- audit.queue → Match

### Key Point
Headers Exchange uses Message Header key-value matching rather than Routing Key matching.

---

## 5. Exchange Types Comparison

| Exchange Type | Routing Method |
|---|---|
| Direct | Exact Routing Key |
| Fanout | Broadcast to all bound queues |
| Topic | Routing Key pattern matching |
| Headers | Message Header matching |

## 6. Important Concepts to Remember

- Exchange decides where messages should be routed.
- Queue stores messages until consumers receive them.
- Binding connects a Queue to an Exchange.
- Routing Key is important for Direct and Topic Exchanges.
- Fanout ignores Routing Keys.
- Headers Exchange uses Message Headers.
- `x-match=all` requires all specified headers to match.
- `x-match=any` requires at least one specified header to match.

## 7. Learning Progress

- [x] Direct Exchange Theory
- [x] Direct Exchange Hands-on
- [x] Multiple Queues with Direct Exchange
- [x] Multiple Bindings
- [x] Fanout Exchange Theory
- [x] Fanout Exchange Hands-on
- [x] Topic Exchange Theory
- [x] Topic Exchange Hands-on
- [x] Headers Exchange Theory
- [x] Headers Exchange Hands-on

**Next Topic:** Durable Queues and Persistent Messages
