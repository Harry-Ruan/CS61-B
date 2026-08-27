package byow.Core;

public class RoomsUnion{
    int[] unions;

    RoomsUnion(int roomNum){
        unions = new int[roomNum];
        for (int i = 0; i < roomNum; i++){
            unions[i] = -1;
        }
    }

    public void connect(int roomIndex1, int roomIndex2){
        if (!isConnected(roomIndex1, roomIndex2)){
            unions[findRoot(roomIndex2)] = findRoot(roomIndex1);
        }
    }

    public boolean isConnected(int roomIndex1, int roomIndex2){
        if (findRoot(roomIndex1) == findRoot(roomIndex2)){
            return true;
        }
        else{
            return false;
        }
    }

    private int findRoot(int curr){
        while (unions[curr] != -1){
            curr = unions[curr];
        }
        return curr;
    }

    public boolean allConnected(){
        int rootCnt = 0;
        for (int i = 0; i < unions.length; i++){
            if (unions[i] == -1){
                rootCnt += 1;
            }
        }
        if (rootCnt == 1){
            return true;
        }
        return false;
    }
}