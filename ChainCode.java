import java.io.*;

public class ChainCode {
    static class point {
        int row;
        int col;

        point(int a, int b) {
            row = a;
            col = b;
        }
    }

    static class CCProperty {
        int label;
        int numPixels;
        int minRows;
        int minCols;
        int maxRows;
        int maxCols;
    }

    static int numCC;
    static CCProperty CC;
    static int numRows;
    static int numCols;
    static int minVal;
    static int maxVal;
    static int imgAry[][];
    static int boundaryAry[][];
    static int CCAry[][];
    static point coordOffset[];

    static int zeroTable[] = {6, 0, 0, 2, 2, 4, 4, 6};
    static point startP;
    static point currentP;
    static point nextP;
    static int lastQ;
    static int nextDir;
    static int PchainDir;

    static void setZero(int Ary[][]) {
        for (int i = 0; i < numRows + 2; i++) {
            for (int j = 0; j < numCols + 1; j++) {
                Ary[i][j] = 0;
            }
        }
    }

    static void loadImage(Scanner File, int Ary[][]) {
        while (File.hasNext()) {
            for (int j = 1; j < numRows + 1; j++) {
                for (int k = 1; k < numCols + 1; k++) {
                    Ary[j][k] = File.nextInt();
                }
            }
        }
    }

    static void reformatPrettyPrint(int Array[][], BufferedWriter File) throws IOException {
        File.write(numRows + " " + numCols + " " + minVal + " " + maxVal);
        File.write('\n');
        for (int i = 1; i < numRows + 1; i++) {
            for (int j = 1; j < numCols + 1; j++) {
                if (Array[i][j] == 0) {
                    File.write("." + " ");
                } else {
                    File.write(Array[i][j] + " ");
                }
            }
            File.write('\n');
        }
    }

    static void loadCCAry(int label, int Array[][]) {
        for (int i = 0; i < numRows + 2; i++) {
            for (int j = 0; j < numCols + 2; j++) {
                if (imgAry[i][j] == label) {
                    Array[i][j] = label;
                }
            }
        }
    }

    static int findNextP(point currentp, int last, BufferedWriter dBf) throws IOException {
        dBf.write("Entering findNextP method" + '\n');
        int index = last;
        boolean found = false;
        int label = CC.label;
        while (found != true) {
            int i = currentP.row + coordOffset[index].row;
            int j = currentP.col + coordOffset[index].col;
            if (imgAry[i][j] == label) {
                PchainDir = index;
                found = true;
            } else {
                index = (index + 1) % 8;
            }
        }
        dBf.write("Leaving findNextP method" + '\n');
        dBf.write("ChainDir = " + PchainDir + '\n');
        return PchainDir;
    }

    static void getChainCode(CCProperty CC, int CCAry[][], BufferedWriter cCF, BufferedWriter dbF) throws IOException {
        dbF.write("Entering getChain Method" + '\n');
        int label = CC.label;
        for (int i = 0; i < numRows + 2; i++) {
            for (int j = 0; j < numCols + 2; j++) {
                if (CCAry[i][j] == label) {
                    cCF.write(label + " " + i + " " + j + " ");
                    startP.row = i;
                    startP.col = j;
                    currentP.row = i;
                    currentP.col = j;
                    lastQ = 4;
                    i += (numRows + 2) - i;
                    j += (numCols + 2) - j;
                }
            }
        }
        int nextQ = (lastQ + 1) % 8;
        PchainDir = findNextP(currentP, nextQ, dbF);
        cCF.write(PchainDir + " ");
        nextP.row = currentP.row + coordOffset[PchainDir].row;
        nextP.col = currentP.col + coordOffset[PchainDir].col;
        currentP = nextP;
        if (PchainDir == 0) {
            lastQ = zeroTable[7];
        } else {
            lastQ = zeroTable[PchainDir - 1];
        }
        dbF.write("lastQ = " + lastQ + " ; " + "nextQ = " + nextQ + "; currentP.row = " + currentP.row + "; currentP.col = " + currentP.col + ": nextP.row = " + nextP.row + ": nextP.col =" + nextP.col + '\n');
        dbF.write("leaving getChainCode" + "\n");

        while (!(currentP.col == startP.col && currentP.row == startP.row)) {
            nextQ = (lastQ + 1) % 8;
            PchainDir = findNextP(currentP, nextQ, dbF);
            cCF.write(PchainDir + " ");
            nextP.row = currentP.row + coordOffset[PchainDir].row;
            nextP.col = currentP.col + coordOffset[PchainDir].col;
            currentP = nextP;
            if (PchainDir == 0) {
                lastQ = zeroTable[7];
            } else {
                lastQ = zeroTable[PchainDir - 1];
            }
            dbF.write("lastQ = " + lastQ + " ; " + "nextQ = " + nextQ + "; currentP.row = " + currentP.row + "; currentP.col = " + currentP.col + ": nextP.row = " + nextP.row + ": nextP.col =" + nextP.col + '\n');
            dbF.write("leaving getChainCode" + "\n");
        }
        cCF.write("577");
        cCF.write('\n');
    }

    static void constructBoundary(Scanner File, int Array[][], BufferedWriter FL) {
        int label = 0;
        int i = 0;
        int j = 0;
        while (File.hasNext()) {
            numRows = File.nextInt();
            numCols = File.nextInt();
            minVal = File.nextInt();
            maxVal = File.nextInt();
            int mno = File.nextInt();
            break;
        }
        while (label != numCC) {
            while (File.hasNext()) {
                label = File.nextInt();
                i = File.nextInt();
                j = File.nextInt();
                break;
            }
            Array[i][j] = label;
            while (File.hasNext()) {
                int code = File.nextInt();
                if (code == 0) {
                    j += 1;
                }
                if (code == 1) {
                    i -= 1;
                    j += 1;
                }
                if (code == 2) {
                    i -= 1;
                }
                if (code == 3) {
                    i -= 1;
                    j -= 1;
                }
                if (code == 4) {
                    j -= 1;
                }
                if (code == 5) {
                    i += 1;
                    j -= 1;
                }
                if (code == 6) {
                    i += 1;
                }
                if (code == 7) {
                    i += 1;
                    j += 1;
                }
                if (code == 577) {
                    break;
                }
                Array[i][j] = label;
            }
        }
    }

    static void pettyprint(int Array[][], BufferedWriter File) throws IOException {
        for (int i = 0; i < Array.length; i++) {
            for (int j = 0; j < Array[0].length; j++) {
                if (Array[i][j] == 0) {
                    File.write(". ");
                } else {
                    File.write("1 ");
                }
            }
            File.write("\n");
        }
    }
}
