//Check if the array is sorted or not  and remove duplicates(type: easy);
import java.util.*;
public class DuplicateRemove{
    public static void MergeSort(ArrayList<Integer> arr, int low, int high){
        if(low >= high) return;
        int mid = (low + high) / 2;
        MergeSort(arr, low, mid);
        MergeSort(arr, mid + 1, high);
        Merge(arr, low, mid, high);
    }

    public static void Merge(ArrayList<Integer> arr, int l, int m, int h){
        ArrayList<Integer> temp = new ArrayList<>();
        int left = l;
        int right = m + 1;

        // merging the elements;
        while(left <= m && right <= h){
            if(arr.get(left) < arr.get(right)){
                temp.add(arr.get(left));
                left++;
            } else {
                temp.add(arr.get(right));
                right++;
            }
        }
        // pushing left over elements to array, if there exist elements;
        while(left <= m){
            temp.add(arr.get(left));
            left++;
        }
        while(right <= h){
            temp.add(arr.get(right));
            right++;
        }
        for(int i = l; i <= h; i++){
            arr.set(i, temp.get(i - l));
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for(int i = 0;i<=n-1;i++){
            arr.add(sc.nextInt());
        }
        MergeSort(arr, 0, n - 1);
        for(int i = 0;i < arr.size()-1;i++){
            for(int j = i+1;j < arr.size();j++){
                if(arr.get(i).equals(arr.get(j))){
                    arr.remove(j);
                    j--; 
                }
            }
        }
        System.out.println("The array after duplicates are removed: ");
        for(int i = 0;i < arr.size();i++){
            System.out.print(arr.get(i)+" ");
        }
    }
}
