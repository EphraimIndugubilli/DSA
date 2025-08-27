import java.util.*;
public class BubbleSort
{   //bubble sort code;
    public static void BubbleSort(int[] arr,int n){
        for(int i = n-1;i>=0;i--){
            for(int j = 0;j<=i-1;j++){
                if(arr[j]> arr[j+1]){
                    int temp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = temp;
                }
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
		BubbleSort(nums,size);
		//output;
		System.out.println("the array after sortting: ");
		for(int i =0;i<size;i++){
		    System.out.println(nums[i]+" ");
		}
	}
}