package Game;

import java.util.Random;

public class CreatMatrixNumber {
    int matrixNumber[][];
    CreatMatrixNumber(int n,int m,int totalNumberIcon){
        boolean checkSelectedIcon[]=new boolean[totalNumberIcon];
        int selectedIcons[]=new int[m*n];
        int massSelectedIcon[]=new int[m*n];

        Random rnd = new Random();
        int seed=java.time.LocalTime.now().getNano();
        rnd.setSeed(seed);

        //set all icon no selected
        for(int i=0;i<checkSelectedIcon.length;i++)
            checkSelectedIcon[i]=false;

        //select half total matrix (n*m/2)
        for(int i=0;i<selectedIcons.length/2;i++) {
            int randomIcon=rnd.nextInt(totalNumberIcon);
            while(checkSelectedIcon[randomIcon])
                randomIcon=rnd.nextInt(totalNumberIcon);
            selectedIcons[i]=randomIcon;
            checkSelectedIcon[randomIcon]=true;
        }

        //duplicate selected icons
        for(int i=selectedIcons.length/2;i<selectedIcons.length;i++)
            selectedIcons[i]=selectedIcons[i-selectedIcons.length/2];

        selectedIcons[0]=51;
        selectedIcons[selectedIcons.length/2]=52;
        //set heavy icon to sort
        for(int i=0;i<selectedIcons.length;i++)
            massSelectedIcon[i]=rnd.nextInt(Integer.MAX_VALUE);

        //sort selected icons
        bubbleSort(selectedIcons,massSelectedIcon);

        //arrange up matrix
        matrixNumber=new int[n][m];
        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
                matrixNumber[i][j]=selectedIcons[i*m+j];
    }

    public int[][] getMatrix(){
        return matrixNumber;
    }
    public void bubbleSort(int array[],int mass[])
    {
        int n = array.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (mass[j] > mass[j+1])
                    SwapElement(array, mass, j, j+1);
    }
    public void SwapElement(int array[],int mass[], int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        temp=mass[i];
        mass[i]=mass[j];
        mass[j]=temp;
    }
}
