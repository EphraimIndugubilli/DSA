//Check if the array is sorted or not 
import java.util.*;
public class CheckSorted{
    public static int isSorted(int[] arr,int n){
        if(n<=1){
            return 1;
        }
        else if(n>1){
            for(int i = 0;i<=n-1;i++){
                for(int j = i+1;j<n;j++){
                    if(arr[i]<arr[j]){
                        continue;
                    }else{
                        return 0;
                    }
                }
            }
        }
        return 1;
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for(int i = 0;i<=n-1;i++){
            arr[i] = sc.nextInt();
        }
        if(isSorted(arr,n) == 1){
            System.out.print("Sorted array");
        }else{
            System.out.print("Not a sorted array");
        }
        
    }
}