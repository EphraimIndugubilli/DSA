//time complexity: O(n^2)
//space complexity: O(1)
import java.util.*;
public class RecursiveInsertionSort
{   public static void InsertionSort(int[] arr,int l,int n ){
        if(l >= n) return;
        int i = l;
        while(i>0 && arr[i-1]>arr[i]){
            int temp = arr[i-1];
            arr[i-1] = arr[i];
            arr[i] = temp;
            i--;
        }
        InsertionSort(arr,l+1,n);
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
		InsertionSort(nums,0,size);
		//output;
		System.out.println("the array after sortting: ");
		for(int i =0;i<size;i++){
		    System.out.printf(nums[i]+" ");
		}
	}
}
