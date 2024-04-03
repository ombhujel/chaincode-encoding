import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner labelFile = new Scanner(new FileReader(args[0]));
        Scanner propFile = new Scanner(new FileReader(args[1]));

        ChainCode chainCode = new ChainCode();

        while (labelFile.hasNext()) {
            ChainCode.numRows = labelFile.nextInt();
            ChainCode.numCols = labelFile.nextInt();
            ChainCode.minVal = labelFile.nextInt();
            ChainCode.maxVal = labelFile.nextInt();
            break;
        }
        while (propFile.hasNext()) {
            ChainCode.numRows = propFile.nextInt();
            ChainCode.numCols = propFile.nextInt();
            ChainCode.minVal = propFile.nextInt();
            ChainCode.maxVal = propFile.nextInt();
            ChainCode.numCC = propFile.nextInt();
            break;
        }

        ChainCode.coordOffset = new ChainCode.point[8];
        ChainCode.coordOffset[0] = new ChainCode.point(0, 1);
        ChainCode.coordOffset[1] = new ChainCode.point(-1, +1);
        ChainCode.coordOffset[2] = new ChainCode.point(-1, 0);
        ChainCode.coordOffset[3] = new ChainCode.point(-1, -1);
        ChainCode.coordOffset[4] = new ChainCode.point(0, -1);
        ChainCode.coordOffset[5] = new ChainCode.point(+1, -1);
        ChainCode.coordOffset[6] = new ChainCode.point(+1, 0);
        ChainCode.coordOffset[7] = new ChainCode.point(+1, +1);

        ChainCode.imgAry = new int[ChainCode.numRows + 2][ChainCode.numCols + 2];
        ChainCode.setZero(ChainCode.imgAry);
        ChainCode.loadImage(labelFile, ChainCode.imgAry);

        BufferedWriter deBugFile = new BufferedWriter(new FileWriter(args[2]));
        ChainCode.reformatPrettyPrint(ChainCode.imgAry, deBugFile);

        ChainCode.CCAry = new int[ChainCode.numRows + 2][ChainCode.numCols + 2];

        String chainCodeFileName = args[1] + "_chainCode.txt";
        BufferedWriter chainCodeFile = new BufferedWriter(new FileWriter(chainCodeFileName));
        String BoundaryFileName = args[1] + "Boundary.txt";
        BufferedWriter BoundaryFile = new BufferedWriter(new FileWriter(BoundaryFileName));
        chainCodeFile.write(ChainCode.numRows + " " + ChainCode.numCols + " " + ChainCode.minVal + " " + ChainCode.maxVal + " " + ChainCode.numCC + '\n');

        for (int i = 0; i < ChainCode.numCC; i++) {
            while (propFile.hasNext()) {
                ChainCode.CC.label = propFile.nextInt();
                ChainCode.CC.numPixels = propFile.nextInt();
                ChainCode.CC.minRows = propFile.nextInt();
                ChainCode.CC.minCols = propFile.nextInt();
                ChainCode.CC.maxRows = propFile.nextInt();
                ChainCode.CC.maxCols = propFile.nextInt();
                break;
            }

            ChainCode.setZero(ChainCode.CCAry);
            ChainCode.loadCCAry(ChainCode.CC.label, ChainCode.CCAry);
            ChainCode.reformatPrettyPrint(ChainCode.CCAry, deBugFile);

            chainCode.getChainCode(ChainCode.CC, ChainCode.CCAry, chainCodeFile, deBugFile);
        }

        chainCodeFile.close();
        Scanner chainCodeFile1 = new Scanner(new FileReader(chainCodeFileName));

        ChainCode.boundaryAry = new int[ChainCode.numRows + 2][ChainCode.numCols + 2];
        ChainCode.setZero(ChainCode.boundaryAry);

        chainCode.constructBoundary(chainCodeFile1, ChainCode.boundaryAry, BoundaryFile);
        chainCode.pettyprint(ChainCode.boundaryAry, BoundaryFile);

        labelFile.close();
        propFile.close();
        deBugFile.close();
        chainCodeFile.close();
        BoundaryFile.close();
    }
}
