import java.util.*;
public class RecursiveBubbleSort
{   public static void BubbleSort(int[] arr,int n ){
        if(n<=0) return;
        for(int i = 0;i<n-1;i++){
            if(arr[i]>arr[i+1]){
                int temp = arr[i];
                arr[i] = arr[i+1];
                arr[i+1] = temp;
            }
        }
        BubbleSort(arr,n-1);
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
		BubbleSort(nums,size);
		//output;
		System.out.println("the array after sortting: ");
		for(int i =0;i<size;i++){
		    System.out.println(nums[i]+" ");
		}
	}
}
