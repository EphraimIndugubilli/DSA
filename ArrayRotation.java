import java.util.*;
public class ArrayRotation{
    public static void rotateArr(int[] arr,int n){
        int temp = arr[0];
        for(int i =0;i<=n-1;i++){
            if(i<n-1){
                for(int j = i+1;j<=n-1;j++){
                    arr[i] = arr[i+1];
                }
            }else{
                arr[i] = temp;
            }
            
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for(int i = 0;i<=n-1;i++){
            arr[i] = sc.nextInt();
        }
        System.err.println("Enter the number of times you want to rotate the array: ");
        int k = sc.nextInt();
        for(int i = 0;i<k;i++){
            rotateArr(arr,n);
        }
        System.out.println("Array after rotation: ");
        for(int i = 0;i<=n-1;i++){
            System.out.print(arr[i] + " ");
        }
    }
}