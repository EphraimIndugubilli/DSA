import java.util.*;
class SecondLargestElement{
    public static int secondLargestElement(int[] nums) {
        int max = 0;
        int secMax = 0;
        
        for(int i = 0; i < nums.length; i++){   
            if(nums[i] > max){
                max = nums[i];
            }
        }
        
        for(int i = 0; i < nums.length; i++){
            if(nums[i] > secMax && nums[i] < max){
                secMax = nums[i];
            }
        }
        if(secMax == 0){
            return -1;
        }
        return secMax;
    }
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++){
            nums[i] = sc.nextInt();
        }
        System.out.println(secondLargestElement(nums));
        sc.close();
    }
}
