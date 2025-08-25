//Selection sort.
import java.util.*;
public class Selectionsort
{   
    public static void Selectionsort(int[] arr,int n){
        for(int i = 0;i<n;i++){
            int min = i;
            //find minimun of array
            for(int j = i;j<n;j++){
                if(arr[j]< arr[min]) {
                    min = j;
                }
            }
            int temp = arr[min];
            arr[min] = arr[i];
            arr[i] = temp;
            
        }
        
    }
	public static void main(String[] args) {
	    Scanner sc= new Scanner(System.in);
		int size = sc.nextInt();
		int a[] = new int[size];
		for(int i = 0;i<size;i++){
		    a[i] = sc.nextInt();
		}
		Selectionsort(a,size);
		System.out.println("arrary after update: ");
        for(int i = 0;i<size;i++){
            System.out.print(a[i]+" ");
        }
	}
}
