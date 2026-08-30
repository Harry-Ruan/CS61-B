# UC Berkeley CS61B — Data Structures and Systems Projects

This repository is a record of my independent study of UC Berkeley's CS61B
Data Structures course (Spring 2021 materials). It contains coursework-based
implementations and experiments in Java, including the larger projects Gitlet
and BYOW.

This is not an independent research project and I was not a member of the
Spring 2021 Berkeley course. The repository started from the public CS61B
course skeleton and retains some staff-provided libraries, interfaces,
rendering code, tests, and harnesses. Those pieces are identified below so
that the scope of my work is clear.

## Portfolio highlights

### Gitlet — a persistent version-control system

`proj2/gitlet/` contains my implementation of a Git-like command-line tool:

- content-addressed blobs identified by SHA-1;
- serialized commits and a persistent staging area;
- branches, checkout, reset, log, status, and commit lookup;
- three-way merge handling with split-point discovery and conflict markers;
- integration testing through the course's command-driven test harness.

The implementation design and persistence model are documented in
[`proj2/gitlet-design.md`](proj2/gitlet-design.md). The main implementation
is in [`Repository.java`](proj2/gitlet/Repository.java), with commit and stage
representations in [`Commit.java`](proj2/gitlet/Commit.java) and
[`Stage.java`](proj2/gitlet/Stage.java).

### BYOW — deterministic procedural world generation

`proj3/byow/Core/` contains my extension of the Build Your Own World project.
Given a seed, it generates non-overlapping rooms, connects them with L-shaped
corridors, materializes reachable floors with flood fill, adds walls and
lights, and places a movable player. The engine also supports deterministic
input-string execution, fog-of-war rendering, player naming, and a simple
save/load path.

The design and control reference is in [`proj3/README.md`](proj3/README.md).
The most relevant files are [`World.java`](proj3/byow/Core/World.java),
[`RoomsUnion.java`](proj3/byow/Core/RoomsUnion.java),
[`Player.java`](proj3/byow/Core/Player.java), and
[`Engine.java`](proj3/byow/Core/Engine.java).

### Supporting data-structure work

The rest of the repository records smaller course exercises: array and linked
deque implementations (`proj1/deque`), a BST map (`lab7/bstmap`), a hash map
with pluggable bucket types (`lab8/hashmap`), a 2048 model (`proj0/game2048`),
and a Karplus–Strong guitar-string exercise (`proj1/gh2`). Labs 1–6 contain
introductory Java, debugging, timing, and persistence exercises.

These components are intentionally presented as coursework rather than as a
single polished library. Some starter or partially completed exercises remain
for study value; the two projects above are the intended technical focus.

## Attribution and scope

- The repository uses the public [Berkeley CS61B Spring 2021
  skeleton](https://github.com/Berkeley-CS61B/skeleton-sp21) and its
  [`library-sp21`](https://github.com/Berkeley-CS61B/library-sp21) submodule.
- Course-provided interfaces, rendering utilities, test fixtures, and support
  code are retained where they are needed to run the assignments.
- `proj3/byow/Networking/` is a historical group extension whose source files
  identify Arjun Sahai and Boren Tsai as authors; it is not claimed as my
  individual work.
- `proj3/byow/TileEngine/`, `proj3/byow/InputDemo/`, and
  `proj3/byow/Core/RandomUtils.java` are course/framework support code. My
  focus in BYOW is the world, player, lighting, and engine behavior layered on
  top of that support.

## Running the focused projects

Initialize the course library submodule first:

```bash
git submodule update --init --recursive
```

Run Gitlet's available integration tests:

```bash
make -C proj2 check
```

Compile and run BYOW's interactive entry point with the course libraries on
the classpath:

```bash
mkdir -p /tmp/cs61b-byow-build
javac -d /tmp/cs61b-byow-build -cp "library-sp21/javalib/*" \
  $(find proj3/byow -name '*.java' -print)
java -cp "/tmp/cs61b-byow-build:library-sp21/javalib/*" byow.Core.Main
```

The GUI requires a desktop Java environment. The command-line input-string
mode is useful for deterministic checks and debugging.

## Verification notes

The repository currently has a small amount of course-harness coverage rather
than a modern CI pipeline. Before describing a behavior in an application, I
would rerun the relevant tests locally and report the exact command and result.
Known follow-up work is listed in the design notes instead of being hidden by
portfolio language.
