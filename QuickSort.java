import java.util.*;

public class QuickSort {
    public static int partition(ArrayList<Integer> arr, int low, int high) {  
        int pivot = arr.get(low);   
        int left = low + 1;
        int right = high;

      
        while (left <= right) {
            while (left <= high && arr.get(left) <= pivot) { 
                left++;
            }
            while (right >= low + 1 && arr.get(right) > pivot) {
                right--;
            }
            if (left < right) {   
                Collections.swap(arr, left, right);
            }
        }
        Collections.swap(arr, low, right); 
        return right;
    }

    public static void qs(ArrayList<Integer> arr, int low, int high) {
        if (low < high) {
            int partind = partition(arr, low, high);
            qs(arr, low, partind - 1);  
            qs(arr, partind + 1, high); 
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the array size: ");
        int n = sc.nextInt();
        System.out.println("Enter the elements of the array: ");
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < n; i++) {   
            arrayList.add(sc.nextInt());
        }

        qs(arrayList, 0, n - 1);  

        for (int i = 0; i < n; i++) {  
            System.out.print(arrayList.get(i) + " ");
        }
    }
}
