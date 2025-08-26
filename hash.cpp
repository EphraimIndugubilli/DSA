#include<bits/stdc++.h>
using namespace std;
//code for precomputation of frequency of elements in an array using hashing;
//if array side of hash decraled globally then it cannpt be limited else if it is mentioned inside the main it can only go upto a range of 1e6(if it crosses that it will throw a segmentation error);
// int main(){
//     int n;
//     cout<<"enter array size"<<endl;
//     cin>>n;
//     int arr[n];
//     cout<<"enter the array inputs"<<endl;
//     for(int i = 0; i<n;i++){
//         cin>>arr[i];
//     }
//     //hashing;
//     cout<<"enter the hashing array size"<<endl;
//     int h;
//     cin>>h;
//     int hash[h] = {0};
//     for(int i = 0;i<n;i++){
//         hash[arr[i]] += 1;
//     }
//     //fetcing
//     int q;
//     cout<<"enter the numbers of query"<<endl;
//     cin>>q;
//      cout<<"enter the queries"<<endl;
//     while(q--){
//         int number;
//         cin>>number;
//         cout<<hash[number]<<endl;
//     }
// }
