//best time complexity is O(n) as there won't be any swapping 
//worst and avg would be O(n^2)
import java.util.*;
public class  InsertionSort
{   
    public static void InsertionSort(int[] arr,int n){
        for(int i=0;i<=n-1;i++){
            int j = i;
            while(j>0 && arr[j-1]>arr[j]){
                int temp = arr[j-1];
                arr[j-1] = arr[j];
                arr[j] = temp;
                j--;
            }
            
        }
    }
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		//user input for array size and integers of array
		int size = sc.nextInt();
		int[] nums = new int[size];
		for(int i=0;i<size;i++){
		    nums[i] = sc.nextInt();
		}
		//initialization of sort;
		InsertionSort(nums,size);
		//output;
		System.out.println("the array after sortting: ");
		for(int i =0;i<size;i++){
		    System.out.println(nums[i]+" ");
		}
	}
}