#include <stdio.h>
#include <iostream>
#include <stdio.h>
#include <iostream>
#include <vector>
#include "opencv2/core.hpp"
#include "opencv2/features2d.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/xfeatures2d.hpp"

#ifdef WIN32
#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#else
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#endif

#define DEFAULT_BUFLEN 512
#define DEFAULT_PORT "12345"


using namespace std;
using namespace cv;
using namespace cv::xfeatures2d;

std::vector<std::string> ImageFiles;
String IdentifyObject( String path);

int GetMatchPoints(String source, String dest)
{
	//Mat img_1 = imread( source, IMREAD_GRAYSCALE );
	//Mat img_2 = imread( dest, IMREAD_GRAYSCALE );
	Mat img_1 = imread( source, IMREAD_LOAD_GDAL);
	Mat img_2 = imread( dest, IMREAD_LOAD_GDAL );
	int matched_points = 0;
	Size size(500,500);
	cv::resize(img_1,img_1,size);
	cv::resize(img_2,img_2,size);
	cv::Ptr<Feature2D> f2d = xfeatures2d::SIFT::create();
	//cv::Ptr<Feature2D> f2d = xfeatures2d::SURF::create();
	//cv::Ptr<Feature2D> f2d = ORB::create();

	std::vector<KeyPoint> keypoints_1, keypoints_2;
	f2d->detect( img_1, keypoints_1 );
	f2d->detect( img_2, keypoints_2 );

	Mat descriptors_1, descriptors_2;
	f2d->compute( img_1, keypoints_1, descriptors_1 );
	f2d->compute( img_2, keypoints_2, descriptors_2 );

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//FlannBasedMatcher matcher;
	BFMatcher matcher;
	std::vector< DMatch > matches;
	matcher.match( descriptors_1, descriptors_2, matches );

	double max_dist = 0; double min_dist = 100;

	//-- Quick calculation of max and min distances between keypoints
	for( int i = 0; i < descriptors_1.rows; i++ )
	{ double dist = matches[i].distance;
	if( dist < min_dist ) min_dist = dist;
	if( dist > max_dist ) max_dist = dist;
	}

	//printf("-- Max dist : %f \n", max_dist );
	//printf("-- Min dist : %f \n", min_dist );

	std::vector< DMatch > good_matches;

	for( int i = 0; i < descriptors_1.rows; i++ )
	{ if( matches[i].distance <= max(2*min_dist, 0.02) )
	{ good_matches.push_back( matches[i]); }
	}

	//-- Draw only "good" matches
	Mat img_matches;
	drawMatches( img_1, keypoints_1, img_2, keypoints_2,
			good_matches, img_matches, Scalar::all(-1), Scalar::all(-1),
			vector<char>(), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS );

	//-- Show detected matches
	//	imshow( "Good Matches", img_matches );
	matched_points = (int)good_matches.size();

	//	for( int i = 0; i < matched_points; i++ )
	//	{ printf( "-- Good Match [%d] Keypoint 1: %d  -- Keypoint 2: %d  \n", i, good_matches[i].queryIdx, good_matches[i].trainIdx ); }

	//	waitKey(0);

	return matched_points;

}

int socketReceiveImage()
{
	printf("======= Socket opened =====\n");
	WSADATA wsaData;
	int iResult;

	SOCKET ListenSocket = INVALID_SOCKET;
	SOCKET ClientSocket = INVALID_SOCKET;

	struct addrinfo *result = NULL;
	struct addrinfo hints;
	// Initialize Winsock
	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed with error: %d\n", iResult);
		return 1;
	}

	ZeroMemory(&hints, sizeof(hints));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_protocol = IPPROTO_TCP;
	hints.ai_flags = AI_PASSIVE;

	// Resolve the server address and port
	iResult = getaddrinfo(NULL, DEFAULT_PORT, &hints, &result);
	if ( iResult != 0 ) {
		printf("getaddrinfo failed with error: %d\n", iResult);
		WSACleanup();
		return 1;
	}

	// Create a SOCKET for connecting to server
	ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);
	if (ListenSocket == INVALID_SOCKET) {
		printf("socket failed with error: %d\n", WSAGetLastError());
		freeaddrinfo(result);
		WSACleanup();
		return 1;
	}

	// Setup the TCP listening socket
	iResult = bind( ListenSocket, result->ai_addr, (int)result->ai_addrlen);
	if (iResult == SOCKET_ERROR) {
		printf("bind failed with error: %d\n", WSAGetLastError());
		freeaddrinfo(result);
		closesocket(ListenSocket);
		WSACleanup();
		return 1;
	}

	freeaddrinfo(result);
	iResult = listen(ListenSocket, 5 );//SOMAXCONN);
	if (iResult == SOCKET_ERROR) {
		printf("listen failed with error: %d\n", WSAGetLastError());
		closesocket(ListenSocket);
		WSACleanup();
		return 1;
	}

	// Accept a client socket
	char recvbuf[DEFAULT_BUFLEN] = {0};
	int recvbuflen = DEFAULT_BUFLEN;

	ClientSocket = accept(ListenSocket, NULL, NULL);
	if (ClientSocket == INVALID_SOCKET) {
		printf("accept failed with error: %d\n", WSAGetLastError());
		closesocket(ListenSocket);
		WSACleanup();
		return 1;
	}

	iResult = recv(ClientSocket, recvbuf, recvbuflen,0);

	printf("Received path is %s\n size is %d\n",recvbuf,iResult);
	recvbuf[iResult] = '\0';
	string str(recvbuf);
	str = str.substr(0,str.find_first_of(".") + 4);
	cout << str;

	String ret = IdentifyObject(str);

	send(ClientSocket,ret.c_str(),ret.size(),0);



	//fclose(fp);

	iResult = shutdown(ClientSocket, SD_SEND);
	if (iResult == SOCKET_ERROR) {
		printf("shutdown failed with error: %d\n", WSAGetLastError());
		closesocket(ClientSocket);
		WSACleanup();
		return 1;
	}

	// cleanup
	closesocket(ClientSocket);

	WSACleanup();
	closesocket(ListenSocket);
	return 0;
}

void GetImages()
{
	HANDLE hFind;
	WIN32_FIND_DATA data;

	hFind = FindFirstFile(".\\Images\\*", &data);
	if (hFind != INVALID_HANDLE_VALUE)
	{
		do {
			if(strcmp(data.cFileName, ".") && strcmp(data.cFileName, ".."))
				ImageFiles.push_back(data.cFileName);
		} while (FindNextFile(hFind, &data));
		FindClose(hFind);
	}
	else
		printf("Invalid Directory\n");

}

String IdentifyObject( String path)
{
	String temp,ret;
	int matchpoints = 0,max = 0,len = ImageFiles.size();

	for(int i=0;i< len;i++)
	{	temp = "./Images/" + ImageFiles[i];
	//printf("%s\n", temp.c_str());
	//matchpoints = GetMatchPoints("./received_file.gif",temp);
	matchpoints = GetMatchPoints(path,temp);
	printf("Path is %s matchpoint is %d\n",temp.c_str(),matchpoints);
	if(matchpoints > max)
	{
		max = matchpoints;
		ret = ImageFiles[i];
	}
	}

	printf("\nObject is %s matchpoints is %d\n",ret.c_str(),matchpoints);
	ret = ret.substr(0,ret.find_last_of("."));
	return ret;
}

int main( int argc, char** argv )
{
	GetImages();
	while(1)
	{
		socketReceiveImage();
	}

	printf("=== Here ===");
	return 0;


}

