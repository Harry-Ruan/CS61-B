package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class World{
    private Long SEED;
    private int WIDTH;
    private int HEIGHT;
    private Random r;
    private ArrayList<Room> worldRooms;
    private ArrayList<Light> worldLights;
    private RoomsUnion roomsUnion;
    private boolean[][] worldDraft;
    private TETile[][] world;
    private TETile[][] worldMap;
    private Player player;

    World(Long seed, int W, int H){
        SEED = seed;
        WIDTH = W;
        HEIGHT = H;
        r = new Random(SEED);
        worldRooms =  new ArrayList<>();
        worldLights = new ArrayList<>();
        worldDraft = new boolean[WIDTH][HEIGHT];
        worldMap = new TETile[WIDTH][HEIGHT];
        for(int x=0;x<WIDTH;x++){
            for(int y=0;y<HEIGHT;y++){
                worldMap[x][y]= Tileset.NOTHING;
            }
        }
    }

    private Room[] randomGenerateRooms(){
        int roomNum;
        do{
            roomNum = r.nextInt(80);
        }while (roomNum < 60);
        Room[] rooms = new Room[roomNum];
        for (int i = 0; i < roomNum; i++){
            int l = r.nextInt(8) + 3;
            int w = r.nextInt(8) + 3;
            rooms[i] = new Room(l, w, r.nextInt(WIDTH - l), r.nextInt(HEIGHT - w - 2), i);
        }
        return rooms;
    }

    public void addRooms(){
        Room[] rooms = randomGenerateRooms();
        int roomNum = rooms.length;
        int worldRoomNum = 0;
        for (int i = 0; i < roomNum; i++){
            /* true 就不加 */
            if (addRoom(rooms[i])){
                /* worldRooms 加这个房间 */
                rooms[i].changeIndex(worldRoomNum);
                worldRooms.add(rooms[i]);
                worldRoomNum += 1;
            }
        }
        /* roomsUnion 初始化这些房间 */
        roomsUnion = new RoomsUnion(worldRoomNum);
    }

    /* the process of adding room can be actually combined into this checking */
    private boolean addRoom(Room room){
        int x = room.X;
        int y = room.Y;
        int l = room.length;
        int w = room.width;
        boolean toAdd = !checkOverlap(room);
        if (toAdd){
            for (int i = 0; i < l; i++){
                for (int j = 0; j < w; j++){
                    worldDraft[i + x][j + y] = true;
                }
            }
        }
        return toAdd;
    }

    private boolean checkOverlap(Room room){
        int x = room.X;
        int y = room.Y;
        int l = room.length;
        int w = room.width;
        for (int i = 0; i < l; i++){
            for (int j = 0; j < w; j++){
                if (worldDraft[i + x][j + y] == true){
                    return true;
                }
            }
        }
        return false;
    }

    public void addHallways(){
        int roomNum = worldRooms.size();
        if (roomNum < 2){
            return;
        }
        while(!roomsUnion.allConnected()){
            Room room1 = worldRooms.get(r.nextInt(roomNum));
            Room room2 = findToConnect(room1);
            addHallway(room1, room2);
            roomsUnion.connect(room1.index, room2.index);
        }
    }

    private Room findToConnect(Room room) {
        Room tempRoom;
        Room finalRoom = null;
        int Mindist = WIDTH + HEIGHT;
        int roomNum = worldRooms.size();

        for (int i = 0; i < roomNum; i++) {
            tempRoom = worldRooms.get(i);
            if (!roomsUnion.isConnected(tempRoom.index, room.index)
                    && Math.abs(tempRoom.X - room.X) + Math.abs(tempRoom.Y - room.Y) < Mindist) {
                Mindist = Math.abs(tempRoom.X - room.X) + Math.abs(tempRoom.Y - room.Y);
                finalRoom = tempRoom;
            }
        }
        return finalRoom;
    }
    /* necessary to pass in worldMap? */
    private void addHallway(Room room1, Room room2){
        if (room1 == room2){
            return;
        }
        int x1 = room1.X;
        int y1 = room1.Y;
        int l1 = room1.length;
        int w1 = room1.width;
        int x2 = room2.X;
        int y2 = room2.Y;
        int l2 = room2.length;
        int w2 = room2.width;
        int randomX1 = x1 + r.nextInt(l1);
        int randomY1 = y1 + r.nextInt(w1);
        int randomX2 = x2 + r.nextInt(l2);
        int randomY2 = y2 + r.nextInt(w2);
        switch (r.nextInt(2)){
            case 0: addHallwayL(randomX1, randomY1, randomX2, randomY2);
                break;
            case 1: addHallwayL(randomX2, randomY2, randomX1, randomY1);
                break;
        }
    }

    private void addHallwayL(int x1, int y1, int x2, int y2){
        /* 1 go vertical and then horrizontal to 2 */
        if (y1 < y2){
            for (int i = y1; i <= y2; i++){
                worldDraft[x1][i] = true;
            }
        }
        else{
            for (int i = y2; i <= y1; i++){
                worldDraft[x1][i] = true;
            }
        }
        if (x1 < x2){
            for (int i = x1; i <= x2; i++){
                worldDraft[i][y2] = true;
            }
        }
        else {
            for (int i = x2; i <= x1; i++){
                worldDraft[i][y2] = true;
            }
        }
    }

    public void addFloors(){
        int init_X = worldRooms.get(0).X + 1;
        int init_Y = worldRooms.get(0).Y + 1;
        addFloor(init_X, init_Y);
    }

    private void addFloor(int x, int y){
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT){
            return;
        }
        if (!worldDraft[x][y] || worldMap[x][y].equals(Tileset.FLOOR)){
            return;
        }
        worldMap[x][y] = Tileset.FLOOR;
        addFloor(x + 1, y);
        addFloor(x - 1, y);
        addFloor(x, y + 1);
        addFloor(x, y - 1);
    }

    public void addWalls(){
        for (int x = 0; x < WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                if (worldMap[x][y].equals(Tileset.FLOOR)){
                    addWall(x, y);
                }
            }
        }
        int init_X = worldRooms.get(0).X + 1;
        int init_Y = worldRooms.get(0).Y + 1;
        addWall(init_X, init_Y);
    }

    private void addWall(int x, int y){
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT){
            return;
        }
        addWallPiece(x + 1, y);
        addWallPiece(x - 1, y);
        addWallPiece(x, y + 1);
        addWallPiece(x, y - 1);
        addWallPiece(x + 1, y - 1);
        addWallPiece(x - 1, y - 1);
        addWallPiece(x + 1, y + 1);
        addWallPiece(x - 1, y + 1);
    }

    private void addWallPiece(int x, int y){
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT){
            return;
        }
        if (worldMap[x][y].equals(Tileset.FLOOR) || worldMap[x][y].equals(Tileset.WALL)){
            return;
        }
        worldMap[x][y] = Tileset.WALL;
    }

    public void generateWorld(){
        addRooms();
        addHallways();
        addFloors();
        addWalls();
        addLights();
        world = TETile.copyOf(worldMap);
    }

    public TETile[][] getWorld(){
        return world;
    }

    public TETile[][] getWorldMap(){return worldMap;}

    public void setPlayer(Player p){
        player = p;
    }

    public void addPlayer(String N){
        int x;
        int y;
        String name;
        if (N.equals("")){
            name = "Player";
        }
        else{
            name = N;
        }
        do{
           x = r.nextInt(WIDTH);
           y = r.nextInt(HEIGHT);
        }while (!worldMap[x][y].equals(Tileset.FLOOR));
        setPlayer(new Player(x, y, 5, name));
    }

    public Player getPlayer(){
        return player;
    }

    public Long getSEED(){
        return SEED;
    }

    public void movePlayer(char command){
        player.move(command, this);
    }

    public void addLightsOnWorld(){
        for (Light l : worldLights){
            turnLight(l);
        }
    }

    private void addLights(){
        int x;
        int y;
        for (Room room : worldRooms){
            x = r.nextInt(room.length) + room.X;
            y = r.nextInt(room.width) + room.Y;
            switch (r.nextInt(5)){
                case 0, 1, 2: worldLights.add(new Light(x, y, room));
                    /* if (worldMap[x][y].equals(Tileset.FLOOR)){
                        worldMap[x][y] = Tileset.LIGHTOFF;  ？？？ */
            }
        }
    }

    public void playerTurnLight(){
        Light targetLight = lightInRange(player.getX(), player.getY());
        if (targetLight != null){
            turnLight(targetLight);
        }
    }

    public Light lightInRange(int x, int y) {
        for (Light l : worldLights) {
            if (Math.abs(l.getX() - x) <= 1
                    && Math.abs(l.getY() - y) <= 1) {
                return l;
            }
        }
        return null;
    }

    public void turnLight(Light l){
        int x = l.getX();
        int y = l.getY();
        l.turn();
        if (l.getStatus()){
            world[x][y] = Tileset.LIGHTON;
            TETile[] originTiles = new TETile[]{Tileset.FLOOR};
            changeSurToLightSur(x, y, 1, l, Tileset.LIGHTSUR1, originTiles);
            changeSurToLightSur(x, y, 2, l, Tileset.LIGHTSUR2, originTiles);
            changeSurToLightSur(x, y, 3, l, Tileset.LIGHTSUR3, originTiles);
            changeSurToLightSur(x, y, 4, l, Tileset.LIGHTSUR4, originTiles);
        }
        else{
            world[x][y] = Tileset.LIGHTOFF;
            TETile[] originTiles = new TETile[]{Tileset.LIGHTSUR1, Tileset.LIGHTSUR2, Tileset.LIGHTSUR3, Tileset.LIGHTSUR4};
            changeSurToLightSur(x, y, 1, l, Tileset.FLOOR, originTiles);
            changeSurToLightSur(x, y, 2, l, Tileset.FLOOR, originTiles);
            changeSurToLightSur(x, y, 3, l, Tileset.FLOOR, originTiles);
            changeSurToLightSur(x, y, 4, l, Tileset.FLOOR, originTiles);
        }
    }

    private void changeSurToLightSur(int x, int y, int step, Light l, TETile targetTile, TETile[] originTiles){
        int surX = x;
        int surY = y;
        for(int i = -step; i <= step; i++){
           for (int j = -step; j <= step; j++){
               surX = x + i;
               surY = y + j;
               if ((surX < WIDTH && surX >= 0 ) && (surY < HEIGHT - 2 && surY >= 0) && !(surX == x && surY == y)){
                   for (TETile t : originTiles){
                       if (world[surX][surY].equals(t) && l.getRoom().contains(surX, surY)){
                           world[surX][surY] = targetTile;
                       }
                   }
               }
           }
        }
    }

    public TETile[][] getFinalWorld() {
        TETile[][] result = TETile.copyOf(world);
        result[player.getX()][player.getY()] = Tileset.AVATAR;
        return result;
    }

}
