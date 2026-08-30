# BYOW project notes

This directory contains my coursework-based implementation for UC Berkeley
CS61B's Build Your Own World project. The public course skeleton supplies the
tile engine, input abstractions, and random-number utility; the implementation
I extended is concentrated in `byow/Core/`.

## Design overview

1. `World` samples candidate rooms from a seeded `Random` instance and rejects
   overlaps using a boolean draft grid.
2. Accepted rooms become vertices in `RoomsUnion`. Randomly selected rooms are
   joined with L-shaped corridors until the room graph is connected.
3. A flood fill converts the connected draft region into floor tiles, after
   which adjacent cells are marked as walls.
4. Lights are associated with rooms and update nearby floor tiles when toggled.
5. `Engine` handles menu/game input, deterministic input strings, player
   movement, fog rendering, and a compact save file containing the seed,
   player position, health value, and name.

The seeded generator is useful for debugging because the same seed produces
the same initial layout. The saved world is reconstructed from the seed and
then the player state is restored; this keeps the save format small, but it
also means future generator changes could make old save files incompatible.

## Controls

- `n` — start a new game, then enter a numeric seed and finish with `s`.
- `l` — load `savefile.txt`.
- `c` — change the saved player's name.
- `v` — toggle fog of war during a game.
- `w`, `a`, `s`, `d` — move the player.
- `t` — toggle a nearby light.
- `:q` — save and quit.

## Scope and attribution

`TileEngine`, `InputDemo`, and `RandomUtils` are retained course/framework
support. The `Networking` directory was added in a historical group commit by
Arjun Sahai and Boren Tsai and is not presented as my individual contribution.
The lab12/lab13 exercises are included as course context, not as separate
research contributions.

## Limitations worth discussing honestly

- There is no dedicated BYOW unit-test suite in this repository yet.
- The save format is a plain text snapshot and does not preserve every runtime
  detail, such as individual light state.
- The project still uses the course's older Java/build layout rather than a
  single modern build tool configuration.
