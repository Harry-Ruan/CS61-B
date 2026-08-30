# Gitlet implementation notes

This document describes the implementation in this repository, rather than
claiming a new version-control algorithm. Gitlet is a coursework project based
on the public UC Berkeley CS61B Spring 2021 specification and skeleton.

## Components and data model

- `Repository` owns the `.gitlet` layout and implements the command-level
  operations.
- `Commit` stores a message, timestamp, one or two parent identifiers, and a
  sorted filename-to-blob map.
- `Stage` stores pending additions as a sorted map and pending removals as a
  set.
- `Utils` provides file operations, serialization helpers, and SHA-1 hashing.

The repository stores commit objects under `.gitlet/commits`, file contents as
content-addressed blobs under `.gitlet/blobs`, branch heads under
`.gitlet/refs/branches`, the current branch name in `.gitlet/HEAD`, and the
staging object in `.gitlet/stage`.

## Request flow for a command

`Main` dispatches the first command-line argument to `Repository`. Mutating
commands load the current stage and HEAD commit, update files or objects, then
persist the new stage and branch pointer. Checkout and reset materialize blob
contents back into the working directory. `status` compares working-tree
hashes with the HEAD and stage maps to classify modified, deleted, staged, and
untracked files.

## Merge behavior

`merge` first rejects a dirty staging area and invalid branch requests. It then
finds a split point by walking both parent graphs, compares the split-point,
current, and given-branch blob maps, and applies the standard cases for
additions, removals, and one-sided modifications. When both sides changed a
file differently, it writes conflict markers containing the current and given
contents, stages the result, and records a merge commit with two parents.

## Persistence and integrity

Blob identifiers are SHA-1 hashes of file contents. A commit identifier is
derived from its message, timestamp, parent identifiers, and sorted blob map.
Objects are serialized with Java's object serialization and reloaded by their
identifier. The save format is intentionally simple and local; it is not
intended to be a secure or networked repository format.

## Verification and follow-up work

The included course harness currently exercises four command scripts through
`make -C proj2 check`. The implementation would benefit from a modern,
repository-local test suite covering malformed argument counts, ambiguous
abbreviated commit IDs, nested paths, merge edge cases, and interrupted writes.
Those are worthwhile engineering follow-ups, not behaviors to imply are
already fully guaranteed.
