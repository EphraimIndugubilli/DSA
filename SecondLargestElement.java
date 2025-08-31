import java.util.*;
class SecondLargestElement{
    public static int secondLargestElement(int[] nums) {
        int max = nums[0];
        int secMax = nums[1];
        
        for(int i = 0; i < nums.length - 1; i++){  
            if(nums[i+1] > nums[i]){
                max = nums[i+1];
            }
        }
        
        for(int i = 0; i < nums.length - 1; i++){  
            if(max > nums[i]){
                if(nums[i+1] > nums[i]){
                    secMax = nums[i+1];
                }
            }
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
