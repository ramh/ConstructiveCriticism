#! /usr/bin/perl
use warnings;
use strict;
use WWW::Mechanize;
use HTTP::Response;

my $root = 'http://news.cnet.com/';
my $url = $root."2547-1_3-0-20.xml?tag=txt";
my $mech = WWW::Mechanize->new();
my $response = $mech->get($url);
my $post;
my $comment;
my $link;
my @comments;
my @responses;


open OUT, ">data/posts2.xml";
print OUT '<?xml version="1.0"?>';
print OUT "\n<posts>\n";


my $post_count=0;
foreach $url($response->decoded_content =~ m/<link>($root[^<]+)<\/link>/g){

	$response = $mech->get($url);
	$post = $response->decoded_content;

	print OUT "\t<post id=\"$url\">\n";

	if($post =~ m/<h1>([^<]+)<\/h1>/s) {print OUT "\t<title>$1</title>\n";}
	if($post =~ m/<div class="postBody" >(.+?)<\/div> <div class="editorBio">/s){
		$_=$1;
		$_=~s/<[^>]+?>//g;
		$_=~s/&/&amp;/g;
		print OUT "\t<body>",$_,"</body>\n";
	}
	

	print OUT "\t<comments>\n"; 
	my $index=-1;
	@comments=();
	@responses=();
	do{
		$response = $mech->get($url);
		$post = $response->decoded_content;

		foreach $comment($post =~ m/<dl messageid="\d+"(.*?>.+?<\/dd>)/gs){
			if($comment =~ m/class="thread">.+?<dd id="body_\d+">(.+?)<\/dd>/s){
				$responses[$index]++;				
			} elsif($comment =~ m/<dd id="body_\d+">(.+?)<\/dd>/s){
				$_=$1;
				$_=~s/&/&amp;/g;				
				push (@comments,$_);
			 	push (@responses,0);
				$index++;
			}
		}

		$link=$mech->find_link( text => 'next');
		$url=$link->url() if (defined $link);
		
	}while(defined $link );

	$index=0;
	foreach (@comments){
		print OUT "\t\t<comment id=\"$index\" responses=\"",$responses[$index],"\">$_</comment>\n";
		$index++;
	}

	print OUT "\t<\/comments>\n";
	print OUT "\t<\/post>\n";
  print "Post done: $post_count\n";
  $post_count++;
}

print OUT "\t<\/posts>\n";


