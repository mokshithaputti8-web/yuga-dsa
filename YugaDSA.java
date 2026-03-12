import java.util.*;

// ================================================================
//   YUGA — Crown Your Mind with AI
//   Java DSA Simulation of the YUGA Website
//
//   PAGES COVERED:
//   1. Login / Signup      → Stack (user session history)
//   2. Home Page           → Doubly Linked List (slider)
//   3. News Feed           → Merge Sort + HashMap (search index)
//   4. Logical Games       → Bubble Sort Visualizer + DSA Quiz + Memory Match
//   5. YUGA AI Assistant   → Max-Heap Priority Queue (message processing)
//   6. Prompt Generator    → Bubble Sort (by usage) + Linked List (saved prompts)
// ================================================================

// ─────────────────────────────────────────────
// DSA #1 — STACK  (Login Session History)
// ─────────────────────────────────────────────
class SessionStack {
    private String[] data;
    private int top;

    SessionStack(int cap) { data = new String[cap]; top = -1; }

    void push(String session) {
        if (top < data.length - 1) data[++top] = session;
    }

    String pop() { return top >= 0 ? data[top--] : null; }
    String peek() { return top >= 0 ? data[top] : null; }
    boolean isEmpty() { return top == -1; }
    int size() { return top + 1; }
}

// ─────────────────────────────────────────────
// DSA #2 — DOUBLY LINKED LIST (Home Slider)
// ─────────────────────────────────────────────
class SlideNode {
    String title, description;
    SlideNode prev, next;

    SlideNode(String t, String d) { title = t; description = d; }
}

class SliderDLL {
    SlideNode head, tail, current;
    int size;

    void add(String title, String desc) {
        SlideNode node = new SlideNode(title, desc);
        if (head == null) { head = tail = current = node; }
        else { tail.next = node; node.prev = tail; tail = node; }
        size++;
    }

    SlideNode forward() {
        if (current.next != null) current = current.next;
        else current = head; // wrap around
        return current;
    }

    SlideNode backward() {
        if (current.prev != null) current = current.prev;
        else current = tail; // wrap around
        return current;
    }

    SlideNode getCurrent() { return current; }
}

// ─────────────────────────────────────────────
// DSA #3 — MERGE SORT + HASHMAP (News Feed)
// ─────────────────────────────────────────────
class NewsArticle {
    int id, score;
    String title, tag, excerpt, date;

    NewsArticle(int id, String title, String tag, String excerpt, String date, int score) {
        this.id = id; this.title = title; this.tag = tag;
        this.excerpt = excerpt; this.date = date; this.score = score;
    }
}

class NewsEngine {
    List<NewsArticle> articles = new ArrayList<>();
    HashMap<String, List<Integer>> searchIndex = new HashMap<>(); // word → article ids

    void addArticle(NewsArticle a) {
        articles.add(a);
        // Build HashMap index
        String[] words = (a.title + " " + a.tag + " " + a.excerpt).toLowerCase().split("\\s+");
        for (String w : words) {
            searchIndex.computeIfAbsent(w, k -> new ArrayList<>()).add(a.id);
        }
    }

    // Merge Sort by date (lexicographic desc = newest first)
    List<NewsArticle> sortByDate() { return mergeSort(new ArrayList<>(articles), "date"); }
    List<NewsArticle> sortByScore() { return mergeSort(new ArrayList<>(articles), "score"); }

    private List<NewsArticle> mergeSort(List<NewsArticle> arr, String key) {
        if (arr.size() <= 1) return arr;
        int mid = arr.size() / 2;
        List<NewsArticle> left = mergeSort(arr.subList(0, mid), key);
        List<NewsArticle> right = mergeSort(arr.subList(mid, arr.size()), key);
        return merge(left, right, key);
    }

    private List<NewsArticle> merge(List<NewsArticle> L, List<NewsArticle> R, String key) {
        List<NewsArticle> res = new ArrayList<>();
        int i = 0, j = 0;
        while (i < L.size() && j < R.size()) {
            boolean pickLeft = key.equals("date")
                ? L.get(i).date.compareTo(R.get(j).date) >= 0
                : L.get(i).score >= R.get(j).score;
            if (pickLeft) res.add(L.get(i++)); else res.add(R.get(j++));
        }
        while (i < L.size()) res.add(L.get(i++));
        while (j < R.size()) res.add(R.get(j++));
        return res;
    }

    // HashMap search — O(1) average lookup
    List<NewsArticle> search(String query) {
        Set<Integer> hitIds = new HashSet<>();
        for (String word : query.toLowerCase().split("\\s+")) {
            for (String key : searchIndex.keySet()) {
                if (key.contains(word)) {
                    hitIds.addAll(searchIndex.get(key));
                }
            }
        }
        List<NewsArticle> results = new ArrayList<>();
        for (NewsArticle a : articles) if (hitIds.contains(a.id)) results.add(a);
        return results;
    }
}

// ─────────────────────────────────────────────
// DSA #4 — BUBBLE SORT VISUALIZER (Games Page)
// ─────────────────────────────────────────────
class BubbleSortVisualizer {
    int[] arr;

    BubbleSortVisualizer(int[] arr) { this.arr = arr.clone(); }

    void visualize() {
        int n = arr.length;
        System.out.println("  Initial: " + Arrays.toString(arr));
        int passes = 0;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    System.out.printf("  Swap arr[%d]=%d <-> arr[%d]=%d  → %s%n",
                        j, arr[j], j+1, arr[j+1], Arrays.toString(arr));
                    int tmp = arr[j]; arr[j] = arr[j+1]; arr[j+1] = tmp;
                    swapped = true;
                }
            }
            passes++;
            if (!swapped) { System.out.println("  ✓ Early exit — already sorted!"); break; }
        }
        System.out.println("  Sorted:  " + Arrays.toString(arr));
        System.out.println("  Passes:  " + passes + "  |  Complexity: O(n²)");
    }
}

// ─────────────────────────────────────────────
// DSA #5 — QUIZ (HashMap for Q&A)
// ─────────────────────────────────────────────
class DSAQuiz {
    static class Question {
        String q; String[] opts; int ans;
        Question(String q, String[] opts, int ans) { this.q=q; this.opts=opts; this.ans=ans; }
    }

    List<Question> questions = new ArrayList<>();
    HashMap<Integer, Boolean> results = new HashMap<>(); // q index → correct?

    void addQuestion(String q, String[] opts, int ans) { questions.add(new Question(q, opts, ans)); }

    void simulate() {
        // Simulate auto-answering for demo output
        int[] userAnswers = {1, 2, 2, 3, 1, 2, 2}; // correct answers
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            boolean correct = (userAnswers[i] == q.ans);
            results.put(i, correct);
            if (correct) score++;
            System.out.printf("  Q%d: %s%n", i+1, q.q);
            System.out.printf("      Answer: %s → %s%n", q.opts[userAnswers[i]], correct ? "✓ Correct" : "✗ Wrong");
        }
        System.out.printf("%n  Final Score: %d/%d  (%.0f%%)%n", score, questions.size(), (score * 100.0 / questions.size()));
    }
}

// ─────────────────────────────────────────────
// DSA #6 — MEMORY MATCH (HashMap state tracking)
// ─────────────────────────────────────────────
class MemoryMatch {
    String[] emojis = {"🌊","⚡","🔴","🌿","🎯","🔷","⭐","🎲"};
    String[] board;
    HashMap<Integer, Boolean> matched = new HashMap<>(); // index → matched?
    int moves = 0;

    void init() {
        board = new String[16];
        for (int i = 0; i < 8; i++) { board[i] = emojis[i]; board[i+8] = emojis[i]; }
        // Simple shuffle
        Random rnd = new Random(42);
        for (int i = 15; i > 0; i--) {
            int j = rnd.nextInt(i+1);
            String tmp = board[i]; board[i] = board[j]; board[j] = tmp;
        }
    }

    void simulateGame() {
        // Simulate finding each pair
        HashMap<String, Integer> firstSeen = new HashMap<>();
        for (int i = 0; i < board.length; i++) {
            if (!matched.containsKey(i)) {
                if (firstSeen.containsKey(board[i])) {
                    int pair = firstSeen.get(board[i]);
                    matched.put(i, true); matched.put(pair, true);
                    moves++;
                    System.out.printf("  Move %2d: Flip [%2d] & [%2d] → %s MATCH!%n", moves, pair, i, board[i]);
                    firstSeen.remove(board[i]);
                } else {
                    firstSeen.put(board[i], i);
                    moves++;
                    System.out.printf("  Move %2d: Flip [%2d] → %s (no match yet)%n", moves, i, board[i]);
                }
            }
        }
        System.out.println("  ✓ All " + emojis.length + " pairs matched in " + moves + " moves!");
        System.out.println("  HashMap tracked " + matched.size() + " matched card states in O(1).");
    }
}

// ─────────────────────────────────────────────
// DSA #7 — MAX-HEAP PRIORITY QUEUE (YUGA Chat)
// ─────────────────────────────────────────────
class ChatMessage implements Comparable<ChatMessage> {
    String text, provider;
    int priority; // 3=high, 2=med, 1=normal

    ChatMessage(String text, String provider, int priority) {
        this.text = text; this.provider = provider; this.priority = priority;
    }

    @Override public int compareTo(ChatMessage o) { return Integer.compare(o.priority, this.priority); }
    String priorityLabel() { return priority == 3 ? "🔴 HIGH" : priority == 2 ? "🟡 MED " : "⚪ LOW "; }
}

class YugaPriorityQueue {
    PriorityQueue<ChatMessage> heap = new PriorityQueue<>();

    void enqueue(ChatMessage msg) { heap.offer(msg); }

    void processAll() {
        System.out.println("  Processing messages by priority (Max-Heap):");
        int order = 1;
        while (!heap.isEmpty()) {
            ChatMessage m = heap.poll();
            System.out.printf("  [%d] %s | [%s] \"%s\"%n", order++, m.priorityLabel(), m.provider, m.text);
        }
    }
}

// ─────────────────────────────────────────────
// DSA #8 — LINKED LIST + BUBBLE SORT (Prompt Generator)
// ─────────────────────────────────────────────
class Prompt {
    String text;
    int uses;
    Prompt next;

    Prompt(String text) { this.text = text; this.uses = 0; }
}

class PromptManager {
    Prompt head;
    int size;

    void save(String text) {
        Prompt p = new Prompt(text);
        if (head == null) { head = p; }
        else { Prompt curr = head; while (curr.next != null) curr = curr.next; curr.next = p; }
        size++;
    }

    void usePrompt(String text) {
        Prompt curr = head;
        while (curr != null) { if (curr.text.equals(text)) { curr.uses++; break; } curr = curr.next; }
        bubbleSort(); // re-sort by usage after each use
    }

    // Bubble Sort on Linked List by uses (desc)
    void bubbleSort() {
        boolean swapped;
        do {
            swapped = false;
            Prompt curr = head;
            while (curr != null && curr.next != null) {
                if (curr.uses < curr.next.uses) {
                    // swap data
                    String tmpText = curr.text; int tmpUses = curr.uses;
                    curr.text = curr.next.text; curr.uses = curr.next.uses;
                    curr.next.text = tmpText; curr.next.uses = tmpUses;
                    swapped = true;
                }
                curr = curr.next;
            }
        } while (swapped);
    }

    void print() {
        Prompt curr = head;
        int rank = 1;
        while (curr != null) {
            System.out.printf("  #%d [%d uses] %s%n", rank++, curr.uses,
                curr.text.length() > 70 ? curr.text.substring(0, 70) + "..." : curr.text);
            curr = curr.next;
        }
    }
}

// ─────────────────────────────────────────────
// MAIN — Simulate all YUGA Pages
// ─────────────────────────────────────────────
public class YugaDSA {

    static void header(String title) {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.printf( "║  %-52s  ║%n", title);
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    static void section(String label) {
        System.out.println("\n  ── " + label + " ──");
    }

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║         YUGA — Crown Your Mind with AI              ║");
        System.out.println("║         Java DSA Website Simulation                 ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // ══════════════════════════════════════════
        // PAGE 1: LOGIN / SIGNUP — Stack
        // ══════════════════════════════════════════
        header("PAGE 1 — LOGIN / SIGNUP  [DSA: Stack]");
        System.out.println("  Concept: Session history stored in a Stack (LIFO).");
        System.out.println("  Each login/signup action is pushed; logout pops the session.\n");

        SessionStack sessions = new SessionStack(10);
        sessions.push("SIGNUP  | user: mokshitha@yuga.ai | role: student");
        sessions.push("LOGIN   | user: mokshitha@yuga.ai | page: home");
        sessions.push("NAVIGATE| user → News Feed");
        sessions.push("NAVIGATE| user → YUGA Assistant");

        System.out.println("  Session History (top = most recent):");
        int n = sessions.size();
        String[] temp = new String[n];
        for (int i = n-1; i >= 0; i--) temp[i] = sessions.pop();
        for (int i = n-1; i >= 0; i--) {
            System.out.printf("  [%d] %s%n", n-i, temp[i]);
            sessions.push(temp[i]);
        }
        System.out.println("\n  Logout: popping current session...");
        System.out.println("  Popped → " + sessions.pop());
        System.out.println("  Current session: " + sessions.peek());
        System.out.println("  Complexity: O(1) push/pop — perfect for undo/back navigation");

        // ══════════════════════════════════════════
        // PAGE 2: HOME — Doubly Linked List (Slider)
        // ══════════════════════════════════════════
        header("PAGE 2 — HOME PAGE  [DSA: Doubly Linked List]");
        System.out.println("  Concept: The 4-slide home carousel uses a Doubly Linked List.");
        System.out.println("  Each slide is a node; prev/next allow bidirectional traversal.\n");

        SliderDLL slider = new SliderDLL();
        slider.add("01 — Merge Sort Powers News Ranking",
            "Articles sorted by date using Merge Sort — O(n log n)");
        slider.add("02 — Binary Search for Instant Results",
            "Keyword search uses Binary Search + Hash Maps — O(1) avg");
        slider.add("03 — Priority Queue for YUGA Messages",
            "Max-Heap priority queue — urgent queries handled first");
        slider.add("04 — AI-Powered Prompt Generation",
            "Saved prompts ranked with Bubble Sort by usage frequency");

        System.out.println("  Current slide: [" + slider.getCurrent().title + "]");
        System.out.println("  → Click NEXT:");
        System.out.println("    Slide: " + slider.forward().title);
        System.out.println("  → Click NEXT:");
        System.out.println("    Slide: " + slider.forward().title);
        System.out.println("  → Click PREV:");
        System.out.println("    Slide: " + slider.backward().title);
        System.out.println("  → Wrap to end (PREV from head):");
        slider.current = slider.head; // go to start
        System.out.println("    Slide: " + slider.backward().title);
        System.out.println("\n  Slider has " + slider.size + " slides (DLL nodes).");
        System.out.println("  Complexity: O(1) forward/backward traversal");

        // ══════════════════════════════════════════
        // PAGE 3: NEWS FEED — Merge Sort + HashMap
        // ══════════════════════════════════════════
        header("PAGE 3 — NEWS FEED  [DSA: Merge Sort + HashMap]");
        System.out.println("  Concept: Articles sorted by date/score with Merge Sort.");
        System.out.println("  Search uses a HashMap word index for near O(1) lookup.\n");

        NewsEngine news = new NewsEngine();
        news.addArticle(new NewsArticle(1, "AI Surpasses Human Performance in Protein Folding","AI Research","Transformer-based models achieve new biology milestone.","2026-03-08",95));
        news.addArticle(new NewsArticle(2, "JavaScript 2026: What's New in ES2026","Web Dev","Pattern matching, temporal API fixes, and immutable records.","2026-03-07",88));
        news.addArticle(new NewsArticle(3, "Quantum Computing Reaches 1000-Qubit Milestone","Quantum","IBM announces breakthrough in error correction.","2026-03-06",92));
        news.addArticle(new NewsArticle(4, "The Rise of Edge AI: Running Models Locally","AI Tools","Full LLM inference on mobile devices without cloud.","2026-03-05",78));
        news.addArticle(new NewsArticle(5, "DSA Fundamentals Still Matter in the Age of AI","Education","Understanding algorithms remains essential even with AI.","2026-03-04",85));
        news.addArticle(new NewsArticle(6, "Open Source Models Match GPT-4 Performance","AI Research","Community-trained models rival proprietary systems.","2026-03-03",90));

        section("Sorted by Date (Merge Sort — newest first)");
        for (NewsArticle a : news.sortByDate())
            System.out.printf("  [%s] %-16s %s%n", a.date, "["+a.tag+"]", a.title);

        section("Sorted by Relevance Score (Merge Sort — highest first)");
        for (NewsArticle a : news.sortByScore())
            System.out.printf("  Score:%3d  [%-12s] %s%n", a.score, a.tag, a.title);

        section("HashMap Search → query: \"AI\"");
        List<NewsArticle> results = news.search("AI");
        System.out.println("  Found " + results.size() + " articles:");
        for (NewsArticle a : results)
            System.out.printf("  → %s [%s]%n", a.title, a.tag);
        System.out.println("\n  HashMap index size: " + news.searchIndex.size() + " words indexed");
        System.out.println("  Merge Sort: O(n log n) | HashMap lookup: O(1) average");

        // ══════════════════════════════════════════
        // PAGE 4: LOGICAL GAMES
        // ══════════════════════════════════════════
        header("PAGE 4 — LOGICAL GAMES");

        // ── Game 1: Bubble Sort Visualizer
        System.out.println("\n  ┌─ GAME 1: Bubble Sort Visualizer ─────────────────────┐");
        int[] gameArr = {64, 34, 25, 12, 22, 11, 90};
        BubbleSortVisualizer viz = new BubbleSortVisualizer(gameArr);
        viz.visualize();

        // ── Game 2: DSA Quiz
        System.out.println("\n  ┌─ GAME 2: DSA Quiz (HashMap tracks results) ──────────┐");
        DSAQuiz quiz = new DSAQuiz();
        quiz.addQuestion("Time complexity of Binary Search?",
            new String[]{"O(n)","O(log n)","O(n²)","O(1)"}, 1);
        quiz.addQuestion("Which uses LIFO?",
            new String[]{"Queue","Array","Stack","Linked List"}, 2);
        quiz.addQuestion("Best case of Bubble Sort?",
            new String[]{"O(n²)","O(n log n)","O(n)","O(1)"}, 2);
        quiz.addQuestion("HashMap average lookup?",
            new String[]{"O(n)","O(log n)","O(n²)","O(1)"}, 3);
        quiz.addQuestion("Left→Root→Right traversal?",
            new String[]{"Preorder","Inorder","Postorder","BFS"}, 1);
        quiz.addQuestion("Merge Sort complexity?",
            new String[]{"O(n²)","O(n)","O(n log n)","O(log n)"}, 2);
        quiz.addQuestion("Priority Queue implemented with?",
            new String[]{"Stack","Array","Heap","Linked List"}, 2);
        quiz.simulate();

        // ── Game 3: Memory Match
        System.out.println("\n  ┌─ GAME 3: Memory Match (HashMap O(1) state tracking) ─┐");
        MemoryMatch mem = new MemoryMatch();
        mem.init();
        System.out.println("  Board (16 cards, shuffled): " + Arrays.toString(mem.board));
        System.out.println();
        mem.simulateGame();

        // ══════════════════════════════════════════
        // PAGE 5: YUGA AI ASSISTANT — Max-Heap Priority Queue
        // ══════════════════════════════════════════
        header("PAGE 5 — YUGA AI ASSISTANT  [DSA: Max-Heap Priority Queue]");
        System.out.println("  Concept: User messages are enqueued with priority.");
        System.out.println("  Questions (?) = HIGH, long messages = MED, short = LOW.");
        System.out.println("  Max-Heap ensures highest priority is always processed first.\n");

        YugaPriorityQueue chatQueue = new YugaPriorityQueue();

        // Simulating messages sent to different AI providers
        chatQueue.enqueue(new ChatMessage("Hey!", "Claude", 1));
        chatQueue.enqueue(new ChatMessage("What is the difference between Stack and Queue?", "Gemini", 3));
        chatQueue.enqueue(new ChatMessage("Explain time complexity and Big-O in detail with examples.", "GPT-4o", 2));
        chatQueue.enqueue(new ChatMessage("Quick question: what does O(1) mean?", "Mistral", 3));
        chatQueue.enqueue(new ChatMessage("Show me a code example of Merge Sort in Java.", "Cohere", 2));
        chatQueue.enqueue(new ChatMessage("Thanks!", "Claude", 1));

        System.out.println("  6 messages queued across: Claude, Gemini, GPT-4o, Mistral, Cohere\n");
        chatQueue.processAll();
        System.out.println("\n  Max-Heap complexity: O(log n) insert, O(log n) extract-max");

        // ══════════════════════════════════════════
        // PAGE 6: PROMPT GENERATOR — LinkedList + Bubble Sort
        // ══════════════════════════════════════════
        header("PAGE 6 — PROMPT GENERATOR  [DSA: Linked List + Bubble Sort]");
        System.out.println("  Concept: Saved prompts stored in a Linked List.");
        System.out.println("  Bubble Sort re-ranks them by usage count after each use.\n");

        PromptManager pm = new PromptManager();
        pm.save("You are an Expert Software Engineer. Explain Binary Search Trees in Java with code example.");
        pm.save("You are a Data Science Researcher. Summarize recent advancements in LLMs step-by-step.");
        pm.save("You are a Mathematics Tutor. Explain Big-O notation in a beginner-friendly way.");
        pm.save("You are a Machine Learning Engineer. Compare supervised vs unsupervised learning with analogies.");

        section("Initial saved prompts (unsorted)");
        pm.print();

        // Simulate users clicking prompts
        pm.usePrompt("You are a Mathematics Tutor. Explain Big-O notation in a beginner-friendly way.");
        pm.usePrompt("You are a Mathematics Tutor. Explain Big-O notation in a beginner-friendly way.");
        pm.usePrompt("You are a Mathematics Tutor. Explain Big-O notation in a beginner-friendly way.");
        pm.usePrompt("You are an Expert Software Engineer. Explain Binary Search Trees in Java with code example.");
        pm.usePrompt("You are an Expert Software Engineer. Explain Binary Search Trees in Java with code example.");
        pm.usePrompt("You are a Machine Learning Engineer. Compare supervised vs unsupervised learning with analogies.");

        section("After usage — Bubble Sorted by use count (most used first)");
        pm.print();
        System.out.println("\n  Bubble Sort: O(n²) | Linked List insert: O(1)");

        // ══════════════════════════════════════════
        // SUMMARY
        // ══════════════════════════════════════════
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              YUGA DSA SUMMARY                       ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  Page              DSA Used            Complexity   ║");
        System.out.println("║  ─────────────     ────────────────    ──────────   ║");
        System.out.println("║  Login/Signup      Stack (LIFO)        O(1)         ║");
        System.out.println("║  Home Slider       Doubly Linked List  O(1)         ║");
        System.out.println("║  News Feed         Merge Sort          O(n log n)   ║");
        System.out.println("║  News Search       HashMap Index       O(1) avg     ║");
        System.out.println("║  Bubble Sort Game  Bubble Sort         O(n²)        ║");
        System.out.println("║  DSA Quiz          HashMap (results)   O(1)         ║");
        System.out.println("║  Memory Match      HashMap (state)     O(1)         ║");
        System.out.println("║  YUGA Chat         Max-Heap PQ         O(log n)     ║");
        System.out.println("║  Prompt Generator  Linked List         O(1)         ║");
        System.out.println("║  Prompt Ranking    Bubble Sort         O(n²)        ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("\n  ✅ All 6 YUGA pages simulated with Java DSA!");
    }
}
